package com.example.a13678.chat_test.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a13678.chat_test.Client;
import com.example.a13678.chat_test.Msg;
import com.example.a13678.chat_test.MsgAdapter;
import com.example.a13678.chat_test.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import top.androidman.SuperButton;

public class ClientActivity extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<Msg>();
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    final static int REQUEST = 10;
    private static  String HOST=null;
    private String Name = null;
    private TextView textView=null;
    private TextView textView_ip=null;
    private EditText editText = null;
    private SuperButton button_send = null;
    private Client client = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Intent intent = getIntent();
        HOST = intent.getStringExtra("IP");
        Name = intent.getStringExtra("Name");
        Toast.makeText(ClientActivity.this,HOST+Name,Toast.LENGTH_LONG).show();

            client = new Client();
            client.clintValue(ClientActivity.this,HOST,23456);
            client.openClientThread();
        editText = findViewById(R.id.edit);
        button_send = findViewById(R.id.send);
        //button_send.setVisibility(View.INVISIBLE);
        button_send.setClickable(false);
        textView_ip = findViewById(R.id.TextView_ip);
        textView_ip.setText("本机IP："+getIpAddressString());
        msgRecyclerView =  findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        boolean flag = true;//client.isClient;
        if(flag)
        {
            Toast toast =  Toast.makeText(ClientActivity.this,"连接成功",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
            //button_send.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast toast =  Toast.makeText(ClientActivity.this,"连接失败",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
        }
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    //server.send_All(content);
                    client.sendMessage(content,2);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    editText.setText(""); // 清空输入框中的内容
                }
            }
        });
        client.mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0://连接成功
                    {
                        button_send.setClickable(true);
                    }
                    case 2://聊天消息
                    {
                        Msg temp_msg = new Msg(msg.obj.toString(), Msg.TYPE_RECEIVED);
                        msgList.add(temp_msg);
                        adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                        msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    }
                }
            }
        };
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        //弹出提示，可以有多种方式
                        Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        finish();
                    }
                    return true;
                }
                return super.onKeyDown(keyCode, event);
            }
    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }
}
