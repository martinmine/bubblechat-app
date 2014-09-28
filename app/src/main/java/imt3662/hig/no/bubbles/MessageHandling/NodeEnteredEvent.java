package imt3662.hig.no.bubbles.MessageHandling;

import android.os.Bundle;

/**
 * Parses the message related to when a node/user enters the area.
 * Created by Martin on 14/09/25.
 */
public class NodeEnteredEvent implements MessageEventParser {
    @Override
    public void parse(MessageEventHandler handler, Bundle message) {
        int userId = Integer.valueOf(message.getString("user_id"));
        handler.nodeEntered(userId);
    }

    @Override
    public String getSignature() {
        return "NODE_ENTER";
    }
}
