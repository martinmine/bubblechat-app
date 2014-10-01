package imt3662.hig.no.bubbles;

import android.os.Bundle;
import android.test.AndroidTestCase;

import com.google.android.gms.maps.model.LatLng;

import imt3662.hig.no.bubbles.MessageHandling.MessageDelegater;
import imt3662.hig.no.bubbles.MessageHandling.MessageEventHandler;

/**
 * Test case for testing the message parsers in the application
 * Created by Martin on 14/09/30.
 */
public class MessageParsingTest extends AndroidTestCase implements MessageEventHandler {
    private static final int userCount = 20;
    private static final int userId = 4;
    private static final String messageText = "Test";
    private static final boolean hasLocation = true;
    private static final double latitude = -0.2d;
    private static final double longitude = 1.21d;
    private static final String username = "Test User";
    private static final int radius = 30;

    private static final ChatMessage chatMessage = new ChatMessage(userId, messageText, hasLocation,
            new LatLng(latitude, longitude), username, 0);

    public MessageParsingTest() {
        MessageDelegater.getInstance().setReceiver(this);
    }

    public void testMessagePosted() {
        Bundle bundle = new Bundle();
        bundle.putString("identifier", "BROADCAST_MESSAGE");
        bundle.putString("user_id", String.valueOf(userId));
        bundle.putString("message_text", messageText);
        bundle.putString("has_location", String.valueOf(hasLocation));
        bundle.putString("latitude", String.valueOf(latitude));
        bundle.putString("longitude", String.valueOf(longitude));
        bundle.putString("username", String.valueOf(username));

        assertTrue(MessageDelegater.getInstance().handleMessage(bundle));
    }

    public void testNodeEntered() {
        Bundle bundle = new Bundle();
        bundle.putString("identifier", "NODE_ENTER");
        bundle.putString("user_id", String.valueOf(userId));

        assertTrue(MessageDelegater.getInstance().handleMessage(bundle));
    }

    public void testNodeLeft() {
        Bundle bundle = new Bundle();
        bundle.putString("identifier", "NODE_LEAVE");
        bundle.putString("user_id", String.valueOf(userId));

        assertTrue(MessageDelegater.getInstance().handleMessage(bundle));
    }

    public void testGotServerInfo() {
        Bundle bundle = new Bundle();
        bundle.putString("identifier", "SERVER_STATUS");
        bundle.putString("user_id", String.valueOf(userId));
        bundle.putString("user_count", String.valueOf(userCount));
        bundle.putString("radius", String.valueOf(radius));

        assertTrue(MessageDelegater.getInstance().handleMessage(bundle));
    }

    @Override
    public void messagePosted(ChatMessage message) {
        assertEquals(userId, message.getUserID());
        assertEquals(messageText, message.getMsg());
        assertEquals(hasLocation, message.isHasLocation());
        assertEquals(String.valueOf(latitude), message.getLatitude());
        assertEquals(String.valueOf(longitude), message.getLongitude());
        assertEquals(username, message.getUsername());
        assertTrue(message.hasFixedColor());
    }

    @Override
    public void nodeEntered(int userId) {
        assertEquals(userId, userId);
    }

    @Override
    public void nodeLeft(int userId) {
        assertEquals(chatMessage.getUserID(), userId);
    }

    @Override
    public void gotServerInfo(int userCount, int userId, int currentRadius) {
        assertEquals(userCount, userCount);
        assertEquals(chatMessage.getUserID(), userId);
        assertEquals(radius, currentRadius);
    }
}
