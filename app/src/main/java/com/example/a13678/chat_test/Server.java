package com.example.a13678.chat_test;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
    public static List<Socket> list;
    public static Executor ex = Executors.newCachedThreadPool();
    public static double[] jd = new double[100];
    public static double[] wd =new double[100];
    public static String[] id =new String [100];
    /*public static String  jd = "108.83,108.85";
    public static String  wd = "34.111,34.111";*/
    public static Handler ServerHandler;
    private ServerSocket server;
    private boolean isClint = false;
    private BufferedReader bufferedReader = null;
    private PrintWriter printWriter;
    private String msg = null;
    static  PrintWriter pw;
    OutputStream outputStream;//输出流
    public Server(int port) {
        try {
            server = new ServerSocket(port);
            isClint = true;
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public  void  listen() {

        //while设置为死循环，等待客户端的socket请求的到来
        new Thread(new Runnable() {
            @Override
            public void run() {
                list=new ArrayList<>();
                try
                {

                    while (true){
                        final Socket socket=server.accept(); //获取套接字
                        list.add(socket);
                        ex.execute(new receive(socket));
                    }
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void returnMessage(String chat){
        Message msg=new Message();
        msg.what = 1;
        msg.obj=chat;
        ServerHandler.sendMessage ( msg );
    }
    public void send_All(String msg) {
        try {
            for (int i = 0; i < Server.list.size(); i++) {
                outputStream = Server.list.get(i).getOutputStream();
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                out.write("2" + "_" +"队长：" + msg+ "\r\n");
                out.flush();
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
class receive implements Runnable {// 用于接收消息
    private Socket so;
    receive(Socket so) {
        this.so = so;
    }
    public void run() {
        BufferedReader in = null;
        try {
            Socket s_temp = this.so;
            in = new BufferedReader(new InputStreamReader(so.getInputStream(), "UTF-8"));
            String s;
            while ((s = in.readLine()) != null) {
                String[] s_arr = s.split("_");
                if(s_arr[0].equals("2"))
                {
                    Message temp = new Message();
                    temp.what = 2;
                    temp.obj = s_temp.getInetAddress()+s_arr[1];
                    Server.ServerHandler.sendMessage ( temp );
                    //new Thread(new send(s_arr[1], so)).start();// 收到消息之后 就把收到的消息发送给除了发送者之外在所有人
                    sendmessage(s_temp.getInetAddress(),s_arr[1]);
                }
               }
        } catch (IOException e) {
            Server.list.remove(so);
           /* String tuichu;
            tuichu = "已退出 用户IP：" + so.getInetAddress().getHostAddress() + "当前连接数：" + Server.list.size();
            System.out.println(tuichu);*/
            try {
                in.close();
                so.close();
            } catch (IOException e1) {
            }
        }
    }
    public void sendmessage(InetAddress ip,final String mes)
    {
        try {
            for (int i = 0; i < Server.list.size(); i++) {
                so = Server.list.get(i);
                if (so.getInetAddress() == ip)
                    continue;// 收到消息之后 就把收到的消息发送给除了发送者之外在所有人。ss为发送者
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(so.getOutputStream(), "UTF-8"));
                out.write("2" + "_" + ip + mes + "\r\n");
                out.flush();
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
class send implements Runnable {// 发送消息
    private String s;
    private Socket ss;
    send(String s, Socket so) {
        this.s = s;
        ss = so;
    }
    public void run() {
        Socket so;
        /*try {
            for (int i = 0; i < Server.list.size(); i++) {
                so = Server.list.get(i);
                if (so == ss)
                    continue;// 收到消息之后 就把收到的消息发送给除了发送者之外在所有人。ss为发送者
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(so.getOutputStream(), "UTF-8"));
                out.write("2" + "_" +ss.getInetAddress() + s + "\r\n");
                out.flush();
                Server.pw = new PrintWriter(new OutputStreamWriter(so.getOutputStream(), "UTF-8"), true);
                Server.pw.println("2" + "_" +ss.getInetAddress() + s);
                Server.pw.flush();
            }
        } catch (IOException e) {
            System.out.println("群发异常");
        }*/
    }
}
class send_all implements Runnable {// 发送消息
    private String s;
    send_all(String s) {
        this.s = s;
    }
    public void run() {
        Socket so;
        try {
            for (int i = 0; i < Server.list.size(); i++) {
                so = Server.list.get(i);
                Server.pw = new PrintWriter(new OutputStreamWriter(so.getOutputStream(), "UTF-8"), true);
                Server.pw.println("2" + "_"  + s);
                Server.pw.flush();
            }
        } catch (IOException e) {
            System.out.println("群发异常");
        }
    }
}