package imt3662.hig.no.bubbles.MessageSerializing;

import imt3662.hig.no.bubbles.MessageErrorListener;

/**
 * Created by Martin on 14/09/26.
 */
public class ServerStatusRequest extends MessageResponse {
    public ServerStatusRequest(double lat, double lng) {
        super();
        setValue("latitude", lat);
        setValue("longitude", lng);
    }

    @Override
    public String getHeader() {
        return "GET_STATUS";
    }
}
