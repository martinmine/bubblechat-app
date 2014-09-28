package imt3662.hig.no.bubbles;

/**
 * Callback related to device registration
 * Created by Martin on 14/09/26.
 */
public interface DeviceRegisteredListener {
    /**
     * Called once we are done registering towards gcm.
     * @param gcmId The gcm id which is a unique id being used when sending messages
     *              through gcm. If the registration failed, this value will be null.
     */
    void registered(String gcmId);
}
