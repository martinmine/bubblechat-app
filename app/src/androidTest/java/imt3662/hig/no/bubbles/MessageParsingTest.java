package imt3662.hig.no.bubbles;

import android.os.Bundle;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import imt3662.hig.no.bubbles.MessageHandling.MessageDelegater;
import imt3662.hig.no.bubbles.MessageHandling.MessageEventHandler;

/**
 * Test case for testing the message parsers in the application
 * Created by Martin on 14/09/30.
 */
public class MessageParsingTest extends AndroidTestCase implements MessageEventHandler {
    private final ChatMessage chatMessage;
    private final int userCount;
    private final int userId;
    private final String messageText;
    private final boolean hasLocation;
    private final double latitude;
    private final double longitude;
    private final String username;

    public MessageParsingTest() {
        this.userCount = 20;
        this.userId = 4;
        this.messageText = "test";
        this.hasLocation = true;
        this.latitude = -0.2d;
        this.longitude = 1.21d;
        this.username = "TestUser";
        this.chatMessage = new ChatMessage(this.userId, this.messageText, this.hasLocation,
                new LatLng(this.latitude, this.longitude), this.username, 0);

        MessageDelegater.getInstance().setReceiver(this);
    }

    public void testMessagePosted() {
        Bundle bundle = new Bundle();
        bundle.putString("identifier", "BROADCAST_MESSAGE");
        bundle.putString("user_id", String.valueOf(this.userId));
        bundle.putString("message_text", this.messageText);
        bundle.putString("has_location", String.valueOf(this.hasLocation));
        bundle.putString("latitude", String.valueOf(this.latitude));
        bundle.putString("longitude", String.valueOf(this.longitude));
        bundle.putString("username", String.valueOf(this.username));

        assertTrue(MessageDelegater.getInstance().handleMessage(bundle));
    }

    public void testNodeEntered() {
        Bundle bundle = new Bundle();
        bundle.putString("identifier", "NODE_ENTER");
        bundle.putString("user_id", String.valueOf(this.userId));

        assertTrue(MessageDelegater.getInstance().handleMessage(bundle));
    }

    public void testNodeLeft() {
        Bundle bundle = new Bundle();
        bundle.putString("identifier", "NODE_LEAVE");
        bundle.putString("user_id", String.valueOf(this.userId));

        assertTrue(MessageDelegater.getInstance().handleMessage(bundle));
    }

    public void testGotServerInfo() {
        Bundle bundle = new Bundle();
        bundle.putString("identifier", "SERVER_STATUS");
        bundle.putString("user_id", String.valueOf(this.userId));
        bundle.putString("user_count", String.valueOf(this.userCount));


        assertTrue(MessageDelegater.getInstance().handleMessage(bundle));
    }

    @Override
    public void messagePosted(ChatMessage message) {
        assertEquals(this.userId, message.getUserID());
        assertEquals(this.messageText, message.getMsg());
        assertEquals(this.hasLocation, message.isHasLocation());
        assertEquals(String.valueOf(this.latitude), message.getLatitude());
        assertEquals(String.valueOf(this.longitude), message.getLongitude());
        assertEquals(this.username, message.getUsername());
        assertTrue(message.hasFixedColor());
    }

    @Override
    public void nodeEntered(int userId) {
        assertEquals(this.userId, userId);
    }

    @Override
    public void nodeLeft(int userId) {
        assertEquals(this.chatMessage.getUserID(), userId);
    }

    @Override
    public void gotServerInfo(int userCount, int userId) {
        assertEquals(this.userCount, userCount);
        assertEquals(this.chatMessage.getUserID(), userId);
    }
}
