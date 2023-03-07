package com.kv.chatbot1;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

public class ChatBotBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "ChatBotBroadcastReceiver";

    // Define the intent filter for the broadcast receiver
    public static IntentFilter getIntentFilter() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ChatBotService.CHATBOT_ACTION);
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        if (intent != null && intent.getAction() != null
                && intent.getAction().equals(ChatBotService.CHATBOT_ACTION)) {

            String message = intent.getStringExtra("message");

            // Send broadcast to update UI
            Intent resultIntent = new Intent("chatbot-result");
            resultIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent);
        }
    }
}
