package imt3662.hig.no.bubbles.MessageHandling;

import imt3662.hig.no.bubbles.ChatMessage;

/**
 * Created by Martin on 14/09/25.
 * The receiver for ever message that can be received from gcm.
 */
public interface MessageEventHandler {
    /**
     * Called when a message is posted in the chat.
     * @param message The chat message posted
     */
    void messagePosted(ChatMessage message);

    /**
     * Called once a node enters the chat and is
     * within the same area as the user's device.
     * @param userId
     */
    void nodeEntered(int userId);

    /**
     * Called once a node exits the application or times out.
     * @param userId
     */
    void nodeLeft(int userId);

    /**
     * Called when we receive an info update from the server.
     * This is also the first message we receive which determines
     * that we have been authenticated to the server.
     * @param userCount Amount of connected nodes/users within the area.
     * @param userId The assigned user/node id that has been given to us.
     */
    void gotServerInfo(int userCount, int userId);
}
