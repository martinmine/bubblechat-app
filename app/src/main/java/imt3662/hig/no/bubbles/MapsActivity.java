package imt3662.hig.no.bubbles;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng userPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
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
        Intent intent = getIntent();
        String radius = intent.getStringExtra("RADIUS");
        String latitude = intent.getStringExtra("LATITUDE");
        String longitude = intent.getStringExtra("LONGITUDE");
        String tracedMessageLatitude = intent.getStringExtra("TRACED_LATITUDE");
        String tracedMessageLongitude = intent.getStringExtra("TRACED_LONGITUDE");
        String tracedMessageUsername = intent.getStringExtra("TRACED_USERNAME");

        if (latitude != null && longitude != null && radius != null
                && !latitude.isEmpty() && !longitude.isEmpty() && !radius.isEmpty()) {
            this.userPosition = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            // Setting user position with marker and circle displaying broadcasting area
            position(this.userPosition, Integer.valueOf(radius));

            if(tracedMessageLatitude != null && !tracedMessageLatitude.isEmpty() &&
                    tracedMessageLongitude != null && !tracedMessageLongitude.isEmpty()) {
                double trackedMessageLatitude = Double.valueOf(tracedMessageLatitude);
                double trackedMessageLongitude = Double.valueOf(tracedMessageLongitude);
                Log.w("TRACK",String.valueOf(trackedMessageLatitude));

                // If a username was found for the tracked message
                if (!tracedMessageUsername.isEmpty()) {
                    setTracedMessageMarker(new LatLng(trackedMessageLatitude,
                        trackedMessageLongitude), tracedMessageUsername);
                } else  // Place pin as Anonymous user
                    setTracedMessageMarker(new LatLng(trackedMessageLatitude,
                        trackedMessageLongitude), getString(R.string.map_anon_user));
            }
        }
    }

    private void position(LatLng latLng, int radius) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.map_marker_you)));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}