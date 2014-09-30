package imt3662.hig.no.bubbles.MessageHandling;

import android.os.Bundle;

/**
 * This message is used for authentication and for updating amount
 * of users/nodes are within the users reach. This message is a response
 * to the ServerStatusRequest and has to be sent at least one time
 * within 2x the server ping cycle-timeout period.
 * Created by Martin on 14/09/25.
 */
public class ServerInfo implements MessageEventParser {
    @Override
    public void parse(MessageEventHandler handler, Bundle message) {
        int userId = Integer.valueOf(message.getString("user_id"));
        int userCount = Integer.valueOf(message.getString("user_count"));
        int radius = Integer.valueOf(message.getString("radius"));
        handler.gotServerInfo(userCount, userId, radius);
    }

    @Override
    public String getSignature() {
        return "SERVER_STATUS";
    }
}
