package imt3662.hig.no.bubbles.MessageHandling;

import android.os.Bundle;

import java.util.Map;

/**
 * Created by Martin on 14/09/25.
 */
public class NodeLeft implements MessageEventParser {
    @Override
    public void parse(MessageEventHandler handler, Bundle message) {
        int userId = Integer.valueOf((String) message.get("user_id"));
        handler.nodeLeft(userId);
    }

    @Override
    public String getSignature() {
        return "NODE_LEAVE";
    }
}
