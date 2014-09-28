package imt3662.hig.no.bubbles;

import java.io.IOException;

/**
 * Delegates calls to a receiver for when an exception
 * occurred while sending a message to gcm.
 * Created by Martin on 14/09/26.
 */
public interface MessageErrorListener {
    void failedToSend(IOException ex);
}
