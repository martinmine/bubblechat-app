package imt3662.hig.no.bubbles.MessageSerializing;

/**
 * This is an exit message which is sent once the user exits the app.
 * Created by Martin on 14/09/26.
 */
public class DestroyNode extends MessageResponse {
    @Override
    public String getHeader() {
        return "DESTROY_NODE";
    }
}
