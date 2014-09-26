package imt3662.hig.no.bubbles.MessageHandling;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 14/09/25.
 */
public class MessageDelegater {
    private MessageEventHandler receiver;
    private Map<String, MessageEventParser> handlers;
    private static MessageDelegater instance;

    public MessageDelegater() {
        handlers = new HashMap<String, MessageEventParser>();

        registerMessage(new ChatMessageEvent());
        registerMessage(new NodeEnteredEvent());
        registerMessage(new NodeLeft());
        registerMessage(new ServerInfo());
    }

    public static MessageDelegater getInstance() {
        if (instance == null)
            instance = new MessageDelegater();
        return instance;
    }

    private void registerMessage(MessageEventParser parser) {
        handlers.put(parser.getSignature(), parser);
    }
    public void setReceiver(MessageEventHandler receiver) {
        this.receiver = receiver;
    }

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
