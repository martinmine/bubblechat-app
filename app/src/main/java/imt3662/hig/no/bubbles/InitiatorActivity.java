package imt3662.hig.no.bubbles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class InitiatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiator);


        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 10 seconds
                    sleep(5*2000);

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
}
