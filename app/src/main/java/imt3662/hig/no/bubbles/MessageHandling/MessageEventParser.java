package imt3662.hig.no.bubbles.MessageHandling;

import android.os.Bundle;

/**
 * Created by Martin on 14/09/25.
 * Parses a message from gcm and forwards it to the proper.
 * MessageEventHandler function
 */
public interface MessageEventParser {
    /**
     * Parses a message and forwards it to the correct MessageEventHandler.
     * @param handler Handler to forward parsed messages to.
     * @param message The message to parse and take data out from.
     */
    void parse(MessageEventHandler handler, Bundle message);

    /**
     * Gets the signature of the message, this is the identifier which
     * is used in the identifier-field in the JSON-message from gcm
     * which identifies which parser it belongs to.
     * @return The identifier/signature.
     */
    String getSignature();
}
