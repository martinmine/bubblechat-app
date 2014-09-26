package imt3662.hig.no.bubbles.MessageSerializing;

import android.os.Bundle;

import java.io.IOException;

import imt3662.hig.no.bubbles.MessageErrorListener;

/**
 * Created by Martin on 14/09/26.
 */
public abstract class MessageResponse {
    private Bundle bundle;

    public MessageResponse() {
        this.bundle = new Bundle();
        this.bundle.putString("id", getHeader());
    }

    protected void setValue(String key, Object value) {
        bundle.putString(key, String.valueOf(value));
    }

    public Bundle getBundle() {
        return bundle;
    }

    public abstract String getHeader();
}
