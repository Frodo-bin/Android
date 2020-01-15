package com.example.a13678.chat_test.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a13678.chat_test.R;

import top.androidman.SuperButton;

public class Init_Client_Activity extends AppCompatActivity {
    private String Name = "";
    private String IP = "";
    private EditText edit_name = null;
    private EditText edit_ip = null;
    private SuperButton connect = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init__client_);
        edit_name = findViewById(R.id.name);
        edit_ip = findViewById(R.id.ip);

        connect = findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = edit_name.getText().toString();
                IP = edit_ip.getText().toString();
                if(!Name.equals("")&&!IP.equals(""))
                {
                    Intent intent = new Intent(Init_Client_Activity.this,ClientActivity.class);
                    intent.putExtra("IP",IP);
                    intent.putExtra("Name",Name);
                    startActivity(intent);
                }
                else if(Name.equals(""))
                    Toast.makeText(Init_Client_Activity.this,"昵称为空",Toast.LENGTH_SHORT).show();
                else if(IP.equals(""))
                    Toast.makeText(Init_Client_Activity.this,"IP为空",Toast.LENGTH_SHORT).show();
                else
                    ;
            }
        });
    }
}
