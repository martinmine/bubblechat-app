package imt3662.hig.no.bubbles;

import android.location.Location;

/**
 * Created by Martin on 14/09/27.
 */
public interface LocationReceiver {
    void locationChanged(Location loc);
}
