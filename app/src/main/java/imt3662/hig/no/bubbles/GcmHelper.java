package imt3662.hig.no.bubbles;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import imt3662.hig.no.bubbles.MessageSerializing.MessageResponse;

/**
 * Created by Martin on 14/09/26.
 */
public class GcmHelper {
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String RECEIVER_ID = "437017129818";
    private String registrationId;

    private GoogleCloudMessaging gcm;
    private AtomicInteger lastMessageId;
    private MessageErrorListener errorListener;

    public GcmHelper(Context context, MessageErrorListener errorListener) {
        this.lastMessageId = new AtomicInteger();
        this.errorListener = errorListener;
        this.gcm = GoogleCloudMessaging.getInstance(context);
    }

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

    public void beginRegistering(SharedPreferences prefs, int currentAppVersion,
                                 DeviceRegisteredListener receiver) {
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        int appVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        if (registrationId.isEmpty() || appVersion != currentAppVersion) {

            // Clear the registration Id, as we dont have app version in our async task
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

    public void setRegistrationId(String id, SharedPreferences prefs) {
        this.registrationId = id;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, id);
        editor.commit();
    }

    public String getRegistrationId() {
        return registrationId;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
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
}
