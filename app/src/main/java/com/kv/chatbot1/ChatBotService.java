package com.kv.chatbot1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

public class ChatBotService extends Service {

    public static final String CHATBOT_ACTION = "com.kv.chatbot1.CHATBOT_ACTION";
    private static final String TAG = "ChatBotService";
    private static final String CHANNEL_ID = "ChatBotChannel";
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals("STOP")) {
                stopService();
            } else {
                startService();
            }
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        stopService();
    }

    private void startService() {
        if (isRunning) {
            Log.d(TAG, "Service already running");
            return;
        }

        isRunning = true;
        Log.d(TAG, "Service starting");

        // Generate message
        String firstName = "User";
        String message = "Hello " + firstName + "!\nHow are you?\nGoodbye " + firstName + "!";

        // Send broadcast to update UI
        Intent intent = new Intent("chatbot-result");
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        // Send notification to user
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("ChatBot")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Notification notification = builder.build();
        startForeground(1, notification);
    }

    private void stopService() {
        if (!isRunning) {
            Log.d(TAG, "Service not running");
            return;
        }

        isRunning = false;
        Log.d(TAG, "Service stopping");

        // Send notification to user
        String message = "ChatBot Stopped: 01"; // Replace 01 with your student number's last two digits
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("ChatBot")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Notification notification = builder.build();
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(2, notification);


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_NAME = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
