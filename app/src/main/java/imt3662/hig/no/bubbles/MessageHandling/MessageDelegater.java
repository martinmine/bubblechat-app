package imt3662.hig.no.bubbles.MessageHandling;

import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Forwards the messages received from gcm to the proper message
 * event handlers if there are any
 * Created by Martin on 14/09/25.
 */
public class MessageDelegater {
    private MessageEventHandler receiver;
    private Map<String, MessageEventParser> handlers;
    private static MessageDelegater instance;

    private MessageDelegater() {
        handlers = new HashMap<String, MessageEventParser>();

        registerMessage(new ChatMessageEvent());
        registerMessage(new NodeEnteredEvent());
        registerMessage(new NodeLeft());
        registerMessage(new ServerInfo());
    }

    /**
     * Gets the MessageDelegater instance (singleton) and makes one if one isn't already made
     * @return The MessageDelegater instance
     */
    public static MessageDelegater getInstance() {
        if (instance == null)
            instance = new MessageDelegater();
        return instance;
    }

    private void registerMessage(MessageEventParser parser) {
        handlers.put(parser.getSignature(), parser);
    }

    /**
     * Sets the receiver for the callback from the message event handlers/parsers:
     * This class delegates the parse operation to the MessageEventParser, then
     * the MessageEventParser calls the proper function in the MessageEventHandler
     * which belongs to what the MessageEventParser parses out from the message bundle.
     * @param receiver
     */
    public void setReceiver(MessageEventHandler receiver) {
        this.receiver = receiver;
    }

    /**
     * Handles a message from gcm and forwards it to the parser if it is a known message.
     * @param message The message to handle
     */
    public void handleMessage(Bundle message) {
        String identifier = message.getString("identifier");
        MessageEventParser parser = this.handlers.get(identifier);

        if (parser != null && this.receiver != null) {
            Log.i("Handling message", identifier);
            parser.parse(this.receiver, message);
        }
        else {
            Log.w("Parser", "Unknown message: " + identifier);
        }
    }
}
