package imt3662.hig.no.bubbles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button settings = (Button) findViewById(R.id.settingsButton);
        Button goMap = (Button) findViewById(R.id.mapButton);


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        goMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude;
                double longitude;

                EditText lati = (EditText) findViewById(R.id.lati);
                latitude = Double.parseDouble(lati.getText().toString());
                EditText longi = (EditText) findViewById(R.id.longi);
                longitude = Double.parseDouble(longi.getText().toString());

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("LATITUDE", latitude);
                intent.putExtra("LONGITUDE", longitude);
                startActivity(intent);
            }
        });







    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
