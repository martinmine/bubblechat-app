package imt3662.hig.no.bubbles.MessageHandling;

import imt3662.hig.no.bubbles.ChatMessage;

/**
 * Created by Martin on 14/09/25.
 */
public interface MessageEventHandler {
    void messagePosted(ChatMessage message);
    void nodeEntered(int userId);
    void nodeLeft(int userId);
    void gotServerInfo(int userCount, int userId);
}
