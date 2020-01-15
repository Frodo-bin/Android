package com.example.a13678.chat_test.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a13678.chat_test.Msg;
import com.example.a13678.chat_test.MsgAdapter;
import com.example.a13678.chat_test.R;
import com.example.a13678.chat_test.Server;

import java.util.ArrayList;
import java.util.List;

import top.androidman.SuperButton;

import static com.example.a13678.chat_test.Activity.ClientActivity.getIpAddressString;

public class ServerActivity extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<Msg>();
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    final static int REQUEST = 10;
    private Server server = new Server(23456);
    private TextView textView_ip = null;//显示IP
    private String ip = "";
    private EditText editText = null;
    private SuperButton send_msg = null;
    private Button locate = null;
    private Button button_all = null;
    private Button map = null;
    private Boolean isconnect = true;
    private String msg = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        textView_ip = findViewById(R.id.TextView_ip);
        //textView_msg = findViewById(R.id.message);
        editText = findViewById(R.id.edit);
        send_msg = findViewById(R.id.send);
        //button_all = findViewById(R.id.send_loc);
        locate = findViewById(R.id.locate);
        map = findViewById(R.id.map);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        initView();
        server.listen();
        server.ServerHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 1:
                    {
                        Toast.makeText(ServerActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                        /*Msg temp_msg = new Msg(msg.obj.toString(), Msg.TYPE_RECEIVED);
                        msgList.add(temp_msg);
                        adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                        msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行*/
                    }
                    case 2:
                    {
                        Msg temp_msg = new Msg(msg.obj.toString(), Msg.TYPE_RECEIVED);
                        msgList.add(temp_msg);
                        adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                        msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    }
                    case 0:
                        isconnect = true;
                }
            }
        };
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                if(!isconnect)
                {
                    Toast.makeText(ServerActivity.this,"没有队员连接进来",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (!"".equals(content)) {
                        Msg msg = new Msg(content, Msg.TYPE_SENT);
                        server.send_All(content);
                        msgList.add(msg);
                        adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                        msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                        editText.setText(""); // 清空输入框中的内容
                    }
                }
            }
        });
    }
    private void initView() {
        ip = getIpAddressString();
        textView_ip.setText("本机IP："+ip);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //server.flag = false;
    }
}
