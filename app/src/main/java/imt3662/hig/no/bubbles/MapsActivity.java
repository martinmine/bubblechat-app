package imt3662.hig.no.bubbles;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("LATITUDE", 0);
        double longitude = intent.getDoubleExtra("LONGITUDE", 0);
        double tracedMessageLatitude = intent.getDoubleExtra("TRACED_LATITUDE", 0);
        double tracedMessageLongitude = intent.getDoubleExtra("TRACED_LONGITUDE", 0);
        String tracedMessageUsername = intent.getStringExtra("TRACKED_USERNAME");

        position(latitude,longitude,2000);

        if (tracedMessageLatitude != 0 && tracedMessageLongitude != 0) {
            if (tracedMessageUsername.length() > 0) {
                setTracedMessageMarker(new LatLng(tracedMessageLatitude, tracedMessageLongitude),
                                                                         tracedMessageUsername);
            }
            else
                setTracedMessageMarker(new LatLng(tracedMessageLatitude, tracedMessageLongitude),
                                                                         "Anonymous user");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
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
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

    }

    private void position(double latitude, double longitude, int radius) {
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))   //set center
                .radius(5000)   //set radius in meters
                .fillColor(0x408FA1F9)  // light blue
                .strokeColor(Color.BLUE)
                .strokeWidth(1);

        mMap.addCircle(circleOptions);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 11.0f);
        mMap.animateCamera(yourLocation);
    }

    private void setTracedMessageMarker(LatLng latLng, String username) {

        mMap.addMarker(new MarkerOptions().position(latLng).title(username));
    }
}
