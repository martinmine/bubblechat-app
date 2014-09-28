package imt3662.hig.no.bubbles.MessageSerializing;

import android.os.Bundle;

import java.io.IOException;

import imt3662.hig.no.bubbles.MessageErrorListener;

/**
 * Message response that is sent to the server, is used for building up the response.
 * Created by Martin on 14/09/26.
 */
public abstract class MessageResponse {
    private Bundle bundle;

    /**
     * Creates a new message response and prepares the required fields for it.
     */
    public MessageResponse() {
        this.bundle = new Bundle();
        this.bundle.putString("id", getHeader());
    }

    protected void setValue(String key, Object value) {
        bundle.putString(key, String.valueOf(value));
    }

    /**
     * Gets the data that is going to be sent to the server.
     * @return
     */
    public Bundle getBundle() {
        return bundle;
    }

    /**
     * The identifier which is used to identify the message server-side.
     * @return
     */
    public abstract String getHeader();
}
