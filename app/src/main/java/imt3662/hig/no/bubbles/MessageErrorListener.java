package imt3662.hig.no.bubbles;

import java.io.IOException;

/**
 * Created by Martin on 14/09/26.
 */
public interface MessageErrorListener {
    void failedToSend(IOException ex);
}
