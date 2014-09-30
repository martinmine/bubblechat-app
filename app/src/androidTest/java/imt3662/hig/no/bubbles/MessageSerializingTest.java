package imt3662.hig.no.bubbles;

import android.os.Bundle;
import android.test.AndroidTestCase;

import com.google.android.gms.maps.model.LatLng;

import imt3662.hig.no.bubbles.MessageSerializing.DestroyNode;
import imt3662.hig.no.bubbles.MessageSerializing.MessageResponse;
import imt3662.hig.no.bubbles.MessageSerializing.PostChatMessage;
import imt3662.hig.no.bubbles.MessageSerializing.ServerStatusRequest;

/**
 * Test case for testing the message serializing
 * Created by Martin on 14/09/30.
 */
public class MessageSerializingTest extends AndroidTestCase {
    public void testDestroyNode() {
        MessageResponse message = new DestroyNode();
        Bundle serializedMessage = message.getBundle();
        assertEquals(serializedMessage.getString("id"), message.getHeader());
    }

    public void testPostChatMessage() {
        final String messageText = "This is a test message";
        final double lat = 12.2d;
        final double lng = 23.2d;
        final boolean showLocation = true;
        final String username = "TestUser";

        MessageResponse message = new PostChatMessage(
                new ChatMessage(0, messageText, showLocation, new LatLng(lat, lng), username, 0));
        Bundle serializedMessage = message.getBundle();

        assertEquals(serializedMessage.getString("id"), message.getHeader());
        assertEquals(serializedMessage.getString("message_text"), messageText);
        assertEquals(serializedMessage.getString("latitude"), String.valueOf(lat));
        assertEquals(serializedMessage.getString("longitude"), String.valueOf(lng));
        assertEquals(serializedMessage.getString("broadcast_location"), String.valueOf(showLocation));
        assertEquals(serializedMessage.getString("username"), username);
    }

    public void testServerStatusRequest() {
        final double lat = 12.2d;
        final double lng = 45.2d;

        MessageResponse message = new ServerStatusRequest(lat, lng);
        Bundle serializedMessage = message.getBundle();

        assertEquals(serializedMessage.getString("id"), message.getHeader());
        assertEquals(serializedMessage.getString("latitude"), String.valueOf(lat));
        assertEquals(serializedMessage.getString("longitude"), String.valueOf(lng));
    }
}
