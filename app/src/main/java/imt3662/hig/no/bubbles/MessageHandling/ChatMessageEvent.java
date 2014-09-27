package imt3662.hig.no.bubbles.MessageHandling;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import imt3662.hig.no.bubbles.ChatMessage;
import imt3662.hig.no.bubbles.ColorRepository;

/**
 * Created by Martin on 14/09/25.
 */
public class ChatMessageEvent implements MessageEventParser {
    @Override
    public void parse(MessageEventHandler handler, Bundle message) {
        int userID = Integer.valueOf(message.getString("user_id"));
        String messageText = message.getString("message_text");
        boolean hasLocation = Boolean.valueOf(message.getString("has_location"));
        double latitude = Double.valueOf(message.getString("latitude"));
        double longitude = Double.valueOf(message.getString("longitude"));
        String username = message.getString("username");
        int color = ColorRepository.getReference().getColor(userID);

        ChatMessage chatMessage = new ChatMessage(userID, messageText, hasLocation,
                new LatLng(latitude, longitude), username, color);
        handler.messagePosted(chatMessage);
    }

    @Override
    public String getSignature() {
        return "BROADCAST_MESSAGE";
    }
}
