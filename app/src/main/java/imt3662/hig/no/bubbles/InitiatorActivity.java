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


public class InitiatorActivity extends Activity implements LocationReceiver, MessageErrorListener, MessageEventHandler, DeviceRegisteredListener {
    private GcmHelper gcm;
    private LocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiator);
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

    @Override
    public void locationChanged(Location loc) {
        if (!gcm.getRegistrationId().isEmpty()) {
            makeContact(new LatLng(loc.getLatitude(), loc.getLongitude()));
        }
        else {
            setLoadingText("Waiting for device registration");
        }
    }

    @Override
    public void failedToSend(IOException ex) {

    }

    @Override
    public void messagePosted(ChatMessage message) {

    }

    @Override
    public void nodeEntered(int userId) {

    }

    @Override
    public void nodeLeft(int userId) {

    }

    @Override
    public void gotServerInfo(int userCount, int userId) {

        setLoadingText("Done!");
        MessageDelegater.getInstance().setReceiver(null);
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("user_count", userCount);
        intent.putExtra("user_id", userId);
        this.locationProvider.destroy();
        startActivity(intent);
        finish();
    }

    private void makeContact(LatLng loc) {
        Log.i("Loader", "Server, you there?");
        setLoadingText("Connecting to server");
        gcm.sendMessage(new ServerStatusRequest(loc.latitude, loc.longitude));
    }

    private void setLoadingText(final String text) {
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
            setLoadingText("Waiting for GPS");
        }
    }

}