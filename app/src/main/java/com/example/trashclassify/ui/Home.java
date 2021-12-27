package com.example.trashclassify.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.trashclassify.ClassifyTest;
import com.example.trashclassify.MainActivity;
import com.example.trashclassify.R;
import com.example.trashclassify.Search;
import com.example.trashclassify.tools.Classfiy;
import com.example.trashclassify.tools.listShow.Garbage_adapter;
import com.example.trashclassify.tools.listShow.TrashNewsAdapter;
import com.example.trashclassify.tools.model.TrashNewsResponse;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Home extends Fragment {
    View view;
    private MyHandler handler;
    private MyHandler myHandler;
    String result;
    TrashNewsResponse list=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        view.setId(R.id.home_paper);
        Button button1= (Button) view.findViewById(R.id.btn1);
        handler = new MyHandler();
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), com.example.trashclassify.Search.class);
                startActivity(intent);
            }
        });
        Button button2 = (Button) view.findViewById(R.id.btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ClassifyTest.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        handler = new MyHandler();
        myHandler = new MyHandler();
        Message message = handler.obtainMessage();
        message.obj = 1;
        message.what = 2;
        handler.sendMessage(message);

    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String str = (String) msg.obj;
                    Log.d("TTTT", result + "  线程名是：" + Thread.currentThread().getName());
                    list = new TrashNewsResponse();
                    list.setList(result);
                    String listName = list.getNewslistTitle().toString();
                    Log.i("json", listName);
                    TrashNewsAdapter trashNewsAdapter = new TrashNewsAdapter(getContext(), R.layout.item_trash_new_rv, list.getNewslist());
                    ListView listView = (ListView) view.findViewById(R.id.rv_news);
                    listView.setAdapter(trashNewsAdapter);
                    break;
                case 2:
                    Log.i("thread", "start!");
                    Thread t = new MyThread();
                    t.start();
                    break;
                default:
                    Log.e("thread", "error!!!! because not put content");
                    break;
            }

        }
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            String httpUrl = "http://api.tianapi.com/lajifenleinews/index?key=601465edd44462e940c17988d0250e21&num=10";
            Log.i("thread", "new success!!");
            BufferedReader reader = null;
            StringBuffer sbf = new StringBuffer();
            try {
                Thread.sleep(2 * 1000);
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sbf.append(strRead);
                    sbf.append("\r\n");
                    Log.e("thread", "success!!!");
                }
                reader.close();
                result = sbf.toString();
                Message message = myHandler.obtainMessage();
                message.obj = result;
                message.what = 1;
                myHandler.sendMessage(message);
                Log.d("TTTT", "这里是发送消息的线程，发送的内容是：" + result + "  线程名是：" + Thread.currentThread().getName());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("thread", e.toString());
            }
        }
    }
}