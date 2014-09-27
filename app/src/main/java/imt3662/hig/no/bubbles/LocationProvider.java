package imt3662.hig.no.bubbles;

import android.content.Context;
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
                double latitude;
                double longitude;

                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                location = new LatLng(latitude, longitude);
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

        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Log.w("PROVIDER","NETWORK IS SET");
        }

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Log.w("PROVIDER","GPS IS SET");
        }
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
}
