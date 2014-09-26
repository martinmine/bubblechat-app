package imt3662.hig.no.bubbles.MessageHandling;

/**
 * Created by Martin on 14/09/25.
 */
public interface MessageEventHandler {
    void messagePosted(int userId, String message, boolean hasLocation, double lat, double lng, String username);
    void nodeEntered(int userId);
    void nodeLeft(int userId);
    void gotServerInfo(int userCount, int userId);
}
