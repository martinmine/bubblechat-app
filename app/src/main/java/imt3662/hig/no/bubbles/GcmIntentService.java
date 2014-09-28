package imt3662.hig.no.bubbles;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import imt3662.hig.no.bubbles.MessageHandling.MessageDelegater;

/**
 * Receives intents and behaves accordingly, also receives
 * messages being sent further to the message parsers.
 * Code from gcm tutorial on https://developer.android.com/google/gcm/client.html
 * Created by Martin on 14/09/24.
 */
public class GcmIntentService extends IntentService {

    public GcmIntentService() {
        super("imt3662.hig.no.bubbles.GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.

        extras.isEmpty();
        String messageType = gcm.getMessageType(intent);
        Log.i("Handling incoming intent", messageType);
        Log.i("Extra: ", extras.toString());

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " +
                //         extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                // Post notification of received message.
                //sendNotification("Received: " + extras.toString());
                Log.i("Service", "Received: " + extras.toString());

                MessageDelegater dispatcher = MessageDelegater.getInstance();

                dispatcher.handleMessage(extras);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}