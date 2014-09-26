package imt3662.hig.no.bubbles.MessageHandling;

import android.os.Bundle;

import java.util.Map;

/**
 * Created by Martin on 14/09/25.
 */
public class ServerInfo implements MessageEventParser {
    @Override
    public void parse(MessageEventHandler handler, Bundle message) {
        int userId = Integer.valueOf(message.getString("user_id"));
        int userCount = Integer.valueOf(message.getString("user_count"));
        handler.gotServerInfo(userCount, userId);
    }

    @Override
    public String getSignature() {
        return "SERVER_STATUS";
    }
}
