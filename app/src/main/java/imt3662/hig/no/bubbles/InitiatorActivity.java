package imt3662.hig.no.bubbles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import imt3662.hig.no.bubbles.MessageHandling.MessageDelegater;
import imt3662.hig.no.bubbles.MessageHandling.MessageEventHandler;
import imt3662.hig.no.bubbles.MessageSerializing.ServerStatusRequest;

/**
 * Loading screen for the application
 */
public class InitiatorActivity extends Activity implements LocationReceiver,
        MessageErrorListener, MessageEventHandler, DeviceRegisteredListener {
    private GcmHelper gcm;
    private LocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiator);

        this.getActionBar().hide();

        if (!GcmHelper.checkPlayServices(this)) {
            // TODO show error message that the user needs to upgrade google play services
            return;
        }

        this.gcm = GcmHelper.get(this, this);

        SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        MessageDelegater.getInstance().setReceiver(this);
        this.gcm.beginRegistering(prefs, getAppVersion(this), this);
        this.locationProvider = LocationProvider.get(this,this);
    }

    /**
     * Gets the current application version from the package info.
     * @return Current application version
     * Code from google tutorial
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Called once we receive any location data, if gcm is ready, start server handshake,
     * otherwise tell the user we are waiting for location services to get ready.
     * @param loc User's location
     */
    @Override
    public void locationChanged(Location loc) {
        if (!gcm.getRegistrationId().isEmpty()) {
            makeContact(new LatLng(loc.getLatitude(), loc.getLongitude()));
        }
        else {
            setLoadingText(R.string.initiator_loading_pending_device_registration);
        }
    }

    /**
     * Called once an error occurs, tells the user that we cannot reach the server.
     * @param ex The exception
     */
    @Override
    public void failedToSend(IOException ex) {
        Log.w("Fatal send error", ex.getMessage());
        //TODO Show a message to the user here
    }

    /**
     * We don't care about this yet, as we are not authenticated towards the server yet
     * @param message The chat message posted
     */
    @Override
    public void messagePosted(ChatMessage message) {

    }

    /**
     * We don't care about this yet, as we are not authenticated towards the server yet
     */
    @Override
    public void nodeEntered(int userId) {

    }

    /**
     * We don't care about this yet, as we are not authenticated towards the server yet
     */
    @Override
    public void nodeLeft(int userId) {

    }

    /**
     * Called once we receive the first authentication message from the server,
     * creates the main activity and sets information that shall be displayed there.
     * @param userCount Amount of connected nodes/users within the area.
     * @param userId The assigned user/node id that has been given to us.
     */
    @Override
    public void gotServerInfo(int userCount, int userId) {
        // go to main activity

        setLoadingText(R.string.initiator_loading_done);
        MessageDelegater.getInstance().setReceiver(null);
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("user_count", userCount);
        intent.putExtra("user_id", userId);
        this.locationProvider.setLocationListener(null);
        startActivity(intent);
        finish();
    }

    private void makeContact(LatLng loc) {
        Log.i("Loader", "Server, you there?");
        setLoadingText(R.string.initiator_loading_text);
        gcm.sendMessage(new ServerStatusRequest(loc.latitude, loc.longitude));
    }

    private void setLoadingText(final int text) {
        Handler handler = new Handler(this.getMainLooper());
        Runnable action = new Runnable() {
            @Override
            public void run() {
                TextView view = (TextView) findViewById(R.id.loadText);
                view.setText(text);
            }
        };

        handler.post(action);
    }

    /**
     * Called once the device is registered on gcm.
     * @param gcmId The gcm id which is a unique id being used when sending messages
     */
    @Override
    public void registered(String gcmId) {
        Log.i("Loader", "Registered on gcm with id " + gcmId);
        // wait for location provider
        LatLng loc = this.locationProvider.getLastKnownLocation();
        if (loc != null) {
            // send registration
            makeContact(loc);
        }
        else {
            // Waiting for gps
            setLoadingText(R.string.initiator_loading_location);
        }
    }
}
    
