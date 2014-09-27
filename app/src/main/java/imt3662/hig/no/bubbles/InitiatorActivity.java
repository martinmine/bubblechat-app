package imt3662.hig.no.bubbles;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

import imt3662.hig.no.bubbles.MessageSerializing.ServerStatusRequest;


public class InitiatorActivity extends Activity implements LocationReceiver, MessageErrorListener {

    private GcmHelper gcm;
    private int currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiator);
        this.gcm = GcmHelper.get(this, this);
        this.currentUserID = -1;

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 10 seconds
                    sleep(2000);

                    // After 5 seconds redirect to another intent
                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

    @Override
    public void locationChanged(Location loc) {

        if (currentUserID == -1 && gcm != null && !gcm.getRegistrationId().isEmpty()) {
            TextView view = (TextView) findViewById(R.id.loadText);
            view.setText(R.string.initiator_loading_location);
            currentUserID = 0;
            gcm.sendMessage(new ServerStatusRequest(loc.getLatitude(), loc.getLongitude()));
        }
    }

    @Override
    public void failedToSend(IOException ex) {

    }
}
