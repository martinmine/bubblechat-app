package imt3662.hig.no.bubbles.MessageSerializing;

import imt3662.hig.no.bubbles.ChatMessage;

/**
 * Serializes a message response containing properties about the user and
 * the chat message the user wants to post to nearby nodes/users.
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
