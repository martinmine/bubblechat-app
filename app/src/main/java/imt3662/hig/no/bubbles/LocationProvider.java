package imt3662.hig.no.bubbles;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Provides location information.
 */
public class LocationProvider {
    private static LocationProvider instance = null;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng location;
    private LocationReceiver listener;

    /**
     * Gets LocationProvider instance
     * @param context The activity context
     * @param listener Who shall receive location updates
     * @return The LocationProvider instance
     */
    public static LocationProvider get(Context context, LocationReceiver listener) throws Exception {
        if (instance == null) {
            instance = new LocationProvider(context, listener);
        }
        return instance;
    }
    private LocationProvider() {}
    private LocationProvider(final Context context, final LocationReceiver listener) throws Exception {
        this.locationManager =  (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.listener = listener;
        this.locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location loc) {
                Log.i("Location provider", "Got location: " + loc.getLatitude() + "," + loc.getLongitude());
                location = new LatLng(loc.getLatitude(), loc.getLongitude());

                if (listener != null)
                    listener.locationChanged(loc);

                if (loc.getAccuracy() < 50.0f) {
                    locationManager.removeUpdates(this);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(context.getApplicationContext(), provider + " enabled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(context.getApplicationContext(), provider + " disabled", Toast.LENGTH_LONG).show();
            }
        };


        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) &&
                                            locationManager.isProviderEnabled("network")) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
            Log.i("PROVIDER", "NETWORK IS SET");
        }
        else if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER) &&
                                                 locationManager.isProviderEnabled("gps")) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
            Log.i("PROVIDER", "GPS IS SET");
        }
        else {
            throw new Exception("No provider available");
        }
    }


    /**
     * Gets the last known location of the user, may be null if nothing has been found.
     * @return The location.
     */
    public LatLng getLastKnownLocation() {
        return location;
    }

    /**
     * Gets the last known latitude.
     * @return Latitude.
     */
    public String getLastKnownLatitude() {
        return String.valueOf(location.latitude);
    }

    /**
     * Gets the last known longitude.
     * @return Longitude.
     */
    public String getLastKnownLongitude() {
        return String.valueOf(location.longitude);
    }

    /**
     * Sets the listener which shall receive location updates.
     * Can be null if we don't want any more updates.
     * @param locationListener The callback object.
     */
    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }
}
