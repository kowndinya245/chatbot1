package com.kv.chatbot1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button generateMsgBtn, stopServiceBtn;
    private TextView msgTextView;

    private ChatBotBroadcastReceiver receiver = new ChatBotBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateMsgBtn = findViewById(R.id.generate_msg_btn);
        stopServiceBtn = findViewById(R.id.stop_service_btn);
        msgTextView = findViewById(R.id.msg_text_view);

        generateMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatBotService();
            }
        });

        stopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopChatBotService();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, ChatBotBroadcastReceiver.getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void startChatBotService() {
        Intent intent = new Intent(this, ChatBotService.class);
        startService(intent);
    }

    private void stopChatBotService() {
        Intent intent = new Intent(this, ChatBotService.class);
        stopService(intent);
    }

    public void updateMessageTextView(String message) {
        msgTextView.setText(message);
    }
}
