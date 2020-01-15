package com.example.a13678.chat_test.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.a13678.chat_test.R;

import top.androidman.SuperButton;

public class SwitchActivity extends AppCompatActivity {
    private SuperButton server = null;
    private SuperButton client = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        server = findViewById(R.id.server);
        client = findViewById(R.id.client);
        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwitchActivity.this,ServerActivity.class);
                startActivity(intent);
            }
        });
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwitchActivity.this,Init_Client_Activity.class);
                startActivity(intent);
            }
        });
    }
}
