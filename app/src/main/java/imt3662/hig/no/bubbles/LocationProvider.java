package imt3662.hig.no.bubbles;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Provides location information.
 */
public class LocationProvider {
    static final int UPDATE_INTERVAL = 180000;

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
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) { }
        };

        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) &&
                                            locationManager.isProviderEnabled("network")) {
            Log.i("PROVIDER", "NETWORK IS SET");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    UPDATE_INTERVAL, 0, locationListener);

        }
        else if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER) &&
                                                 locationManager.isProviderEnabled("gps")) {
            Log.i("PROVIDER", "GPS IS SET");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    UPDATE_INTERVAL, 0, locationListener);
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
