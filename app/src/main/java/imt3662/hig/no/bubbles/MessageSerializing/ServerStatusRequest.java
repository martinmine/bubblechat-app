package imt3662.hig.no.bubbles.MessageSerializing;

/**
 * This message requests the status of the server, and updates
 * the users location.
 * It is used for both ping and authentication towards the server.
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
