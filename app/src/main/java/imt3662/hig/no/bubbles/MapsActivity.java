package imt3662.hig.no.bubbles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng userPosition;
    private Sensor compass;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.compass = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (mMap != null && sensorEvent.values.length > 0) {
                    updateCamera(sensorEvent.values[0]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) { }
        };
        sensorManager.registerListener(this.sensorEventListener, compass, SensorManager.SENSOR_DELAY_NORMAL);

        if (compass == null) {
            Toast.makeText(getApplicationContext(), getText(R.string.error_compass_not_fount), Toast.LENGTH_SHORT).show();
        }
    }

    // Cose based on http://stackoverflow.com/a/14831267
    private void updateCamera(float bearing) {
        CameraPosition currentPlace = new CameraPosition.Builder()
                .target(this.userPosition)
                .bearing(bearing).tilt(65.5f).zoom(18f).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     */
    private void setUpMap() {
        int userRadius;
        double userLatitude;
        double userLongitude;
        double trackedMessageLatitude;
        double trackedMessageLongitude;

        Intent intent = getIntent();
        String radius = intent.getStringExtra("RADIUS");
        String latitude = intent.getStringExtra("LATITUDE");
        String longitude = intent.getStringExtra("LONGITUDE");
        String tracedMessageLatitude = intent.getStringExtra("TRACED_LATITUDE");
        String tracedMessageLongitude = intent.getStringExtra("TRACED_LONGITUDE");
        String tracedMessageUsername = intent.getStringExtra("TRACED_USERNAME");

        if (latitude != null && longitude != null && radius != null) {
            if (latitude.length() > 0 && longitude.length() > 0) {
                userRadius = Integer.parseInt(radius);
                userLatitude = Double.parseDouble(latitude);
                userLongitude = Double.parseDouble(longitude);
                this.userPosition = new LatLng(userLatitude, userLongitude);
                // Setting user position with marker and circle displaying broadcasting area
                position(this.userPosition, 10000);

                if(tracedMessageLatitude != null && tracedMessageLongitude != null) {
                    if (tracedMessageLatitude.length() > 0 && tracedMessageLongitude != null) {
                        trackedMessageLatitude = Double.parseDouble(tracedMessageLatitude);
                        trackedMessageLongitude = Double.parseDouble(tracedMessageLongitude);
                        // If a username was found for the tracked message
                        if (tracedMessageUsername.length() > 0) {
                            setTracedMessageMarker(new LatLng(trackedMessageLatitude,
                                     trackedMessageLongitude), tracedMessageUsername);
                        } else  // Place pin as Anonymous user
                            setTracedMessageMarker(new LatLng(trackedMessageLatitude,
                                          trackedMessageLongitude), "Anonymous user");
                    }
                }
            }
        }
    }

    private void position(LatLng latLng, int radius) {

        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)   //set center
                .radius(radius)   //set radius in meters
                .fillColor(0x408FA1F9)  // light blue
                .strokeColor(Color.BLUE)
                .strokeWidth(1);

        mMap.addCircle(circleOptions);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 10.0f);
        mMap.animateCamera(yourLocation);
    }

    private void setTracedMessageMarker(LatLng latLng, String username) {

        mMap.addMarker(new MarkerOptions().position(latLng).title(username));
    }
}
