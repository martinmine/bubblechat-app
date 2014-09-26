package imt3662.hig.no.bubbles.MessageSerializing;

import imt3662.hig.no.bubbles.ChatMessage;
import imt3662.hig.no.bubbles.MessageErrorListener;

/**
 * Created by Martin on 14/09/26.
 */
public class PostChatMessage extends MessageResponse {
    public PostChatMessage(ChatMessage message) {
        setValue("message_text", message.getMsg());
        setValue("latitude", message.getLatitude());
        setValue("longitude", message.getLongitude());
        setValue("broadcast_location", message.isHasLocation());
        setValue("username", message.getUsername());
    }

    @Override
    public String getHeader() {
        return "POST_MESSAGE";
    }
}
