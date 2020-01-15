package com.example.a13678.chat_test;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
public class Client {
    private Socket client;
    private Context context;
    private String site;           //IP
    private int port;            //端口
    private Thread thread;
    public static Handler mHandler;
    private boolean isClient=false;
    private PrintWriter out;
    private BufferedReader bufferedReader;
    private InputStream in;
    private String str;
    public Double j,w;
    public Double[] jw;
    public Client()
    {
        ;
    }
    public void openClientThread()
    {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    client = new Socket(site,port);
                    if(client!=null)
                    {
                        //Toast.makeText(context,"连接成功",Toast.LENGTH_SHORT).show();
                        isClient = true;
                        forIn();
                    }
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    public void forIn()
    {
        int i = 1;
        while (isClient)
        {
            try {
                bufferedReader =new BufferedReader(new InputStreamReader(client.getInputStream()));
                str = bufferedReader.readLine();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
            if(str!=null)
            {
                String[] s = str.split("_");
                if(s[0].equals("2"))
                {
                    Message msg = new Message();
                    msg.obj = s[1];
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                }
                if(s[0].equals("1"))
                {
                    ;
                }
            }
            i++;
        }
    }
    public void sendMessage(final String chat, final int flag)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    out = new PrintWriter(
                            new OutputStreamWriter(client.getOutputStream()));
                    if(flag==2)
                        out.println("2"+"_"+chat);
                    else if(flag==1)
                        out.println("1"+"_"+chat);
                    out.flush();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    public void clintValue(Context context, String site, int port)
    {
        this.context=context;
        this.site=site;
        this.port=port;
    }
}
