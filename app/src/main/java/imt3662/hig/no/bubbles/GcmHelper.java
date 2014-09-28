package imt3662.hig.no.bubbles;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import imt3662.hig.no.bubbles.MessageSerializing.MessageResponse;
import imt3662.hig.no.bubbles.MessageSerializing.ServerStatusRequest;

/**
 * This helper/manager/facade is responsible for most of the interaction towards
 * Google Cloud Messaging (gcm)
 * Created by Martin on 14/09/26.
 */
public class GcmHelper {
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String RECEIVER_ID = "437017129818";

    private static GcmHelper instance = null;

    private String registrationId;
    private GoogleCloudMessaging gcm;
    private AtomicInteger lastMessageId;
    private MessageErrorListener errorListener;
    private Timer pinger;

    private GcmHelper() { }
    private GcmHelper(Context context) {
        this.lastMessageId = new AtomicInteger();
        this.registrationId = "";
        this.gcm = GoogleCloudMessaging.getInstance(context);
    }

    /**
     * Gets a singleton instance of the GcmHelper object
     * @param context Context to the activity requesting, can be null if the instance is initialized
     * @param errorListener The class which shall receive calls when failed to send messages
     * @return A gcm instance
     */
    public static GcmHelper get(Context context, MessageErrorListener errorListener) {
        if (instance == null) {
            instance = new GcmHelper(context);
        }

        instance.errorListener = errorListener;
        return instance;
    }

    /**
     * Send a message through gcm
     * @param message The message object to be sent
     */
    public void sendMessage(MessageResponse message) {
        Log.i("Sending message", message.getHeader());

        new AsyncTask<MessageResponse, Void, Void>() {
            @Override
            protected Void doInBackground(MessageResponse... params) {
                assert (params.length == 1);

                int messageId = lastMessageId.incrementAndGet();
                MessageResponse message = params[0];

                try {
                    gcm.send(RECEIVER_ID + "@gcm.googleapis.com", String.valueOf(messageId), message.getBundle());
                } catch (IOException e) {
                    errorListener.failedToSend(e);
                }

                return null;
            }
        }.execute(message);
    }

    /**
     * Begins registering the device with gcm, if the device is already registered, it will
     * skip this step and instantly call the receiver.registered with the registration ID.
     * Otherwise, a new async task will be spawned which gets registers the device and sets the
     * settings in the shared preferences object. If we failed to get the gcm key, the key
     * passed to receiver will be null.
     * @param prefs Preferences object which stores information about the key
     * @param currentAppVersion The current app version
     * @param receiver Callback function which is called if or when we have the gcm key
     */
    public void beginRegistering(SharedPreferences prefs, int currentAppVersion,
                                 DeviceRegisteredListener receiver) {
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        int appVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        if (registrationId.isEmpty() || appVersion != currentAppVersion) {
            // Clear gcm reg-id and save the app version, as we
            // don't have the app version in the async task worker
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PROPERTY_REG_ID, "");
            editor.putInt(PROPERTY_APP_VERSION, appVersion);
            editor.commit();

            new AsyncTask<Object, Void, Void>() {
                @Override
                protected Void doInBackground(Object... params) {
                    assert (params.length == 2);
                    assert (params[0] instanceof SharedPreferences);
                    assert (params[1] instanceof DeviceRegisteredListener);

                    SharedPreferences prefs = (SharedPreferences) params[0];
                    DeviceRegisteredListener receiver = (DeviceRegisteredListener) params[1];

                    try {
                        String regId = gcm.register(RECEIVER_ID);
                        setRegistrationId(regId, prefs);
                        receiver.registered(regId);
                    } catch (IOException ex) {
                        Log.w("gcm", "failed registering: " + ex.getMessage());
                        receiver.registered(null);
                    }

                    return null;
                }
            }.execute(prefs, receiver);
        }
        else {
            this.registrationId = registrationId;
            receiver.registered(registrationId);
        }
    }

    /**
     * Sets and stores the registration id towards gcm.
     * @param id The gcm registration id/key
     * @param prefs Preferences object which stores information about the key
     */
    public void setRegistrationId(String id, SharedPreferences prefs) {
        this.registrationId = id;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, id);
        editor.commit();
    }

    /**
     * Gets the gcm registration id
     * @return
     */
    public String getRegistrationId() {
        return registrationId;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     *
     * This code is from the gcm tutorial:
     *     https://developer.android.com/google/gcm/client.html
     */
    public static boolean checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Start pining the server if we haven't started doing so already. This is required unless
     * we want to be kicked out from the server every minute.
     */
    public void startPinging() {
        if (this.pinger == null) {
            final Handler handler = new Handler();
            this.pinger = new Timer();

            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            Log.w("Pinger", "PING!");
                            LocationProvider provider = LocationProvider.get(null, null);
                            sendMessage(new ServerStatusRequest(provider.getLastKnownLocation().latitude,
                                    provider.getLastKnownLocation().longitude));
                        }
                    });
                }
            };

            pinger.schedule(doAsynchronousTask, 10000, 30000);
        }
    }
}
