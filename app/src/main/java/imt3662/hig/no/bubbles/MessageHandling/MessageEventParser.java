package imt3662.hig.no.bubbles.MessageHandling;

import android.os.Bundle;

/**
 * Created by Martin on 14/09/25.
 */
public interface MessageEventParser {
    void parse(MessageEventHandler handler, Bundle message);
    String getSignature();
}
