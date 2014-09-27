package imt3662.hig.no.bubbles;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class LocationProvider {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng location;
    private LocationReceiver listener;

    public LocationProvider(final Context context, final LocationReceiver listener) {
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
            public void onProviderEnabled(String provider) {
                Toast.makeText(context.getApplicationContext(), provider + " enabled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(context.getApplicationContext(), provider + " disabled", Toast.LENGTH_LONG).show();
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        /*
        Joakim should feel free to fix this tomorrow
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);

        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) &&
                                            locationManager.isProviderEnabled("network")) {
            if (locationManager.getBestProvider(criteria, true).equals("network")) {
                                                                                      // Minimum update time
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1 * 30 * 1000, 0, locationListener);

                Log.i("PROVIDER", "NETWORK IS SET");
            }
        }

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER) &&
                                            locationManager.isProviderEnabled("gps")) {
            if (locationManager.getBestProvider(criteria, true).equals("gps")) {
                                                                                    // Minimum update time
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1 * 30 * 1000, 0, locationListener);

                Log.i("PROVIDER", "GPS IS SET");
            }
        }*/
    }

    public LatLng getLastKnownLocation() {
        return location;
    }

    public String getLastKnownLatitude() {
        return String.valueOf(location.latitude);
    }

    public String getLastKnownLongitude() {
        return String.valueOf(location.longitude);
    }

    public void destroy() {
        locationManager.removeUpdates(locationListener);
    }
}
