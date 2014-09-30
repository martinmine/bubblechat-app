package imt3662.hig.no.bubbles;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Receives intents from the gcm service, this code is copied from the gcm tutorial on:
 *     https://developer.android.com/google/gcm/client.html
 * Created by Martin on 14/09/24.
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 88s
        intent.setClass(context, GcmIntentService.class);
        context.startService(intent);
    }
}