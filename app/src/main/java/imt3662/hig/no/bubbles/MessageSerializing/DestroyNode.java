package imt3662.hig.no.bubbles.MessageSerializing;

/**
 * Created by Martin on 14/09/26.
 */
public class DestroyNode extends MessageResponse {
    @Override
    public String getHeader() {
        return "DESTROY_NODE";
    }
}
