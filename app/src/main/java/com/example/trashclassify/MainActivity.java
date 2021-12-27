package com.example.trashclassify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trashclassify.tools.listShow.TrashNewsAdapter;
import com.example.trashclassify.tools.model.TrashNewsResponse;
import com.example.trashclassify.ui.Home;
import com.example.trashclassify.ui.Setting;
import com.flyco.tablayout.SlidingTabLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private SlidingTabLayout slidingTabLayout;
    private ArrayList<Fragment> mFragments;
    private MyHandler handler;
    public TrashNewsResponse list=null;
    String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidingTabLayout = findViewById(R.id.stl_main);
        mViewPager = findViewById(R.id.vp);
        mFragments = new ArrayList<>();
        mFragments.add(new Home());
        mFragments.add(new Setting());
//      无需编写适配器，一行代码关联TabLayout与ViewPager
        slidingTabLayout.setViewPager(mViewPager, new String[]{"首页", "设置"}, this, mFragments);


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
                    TrashNewsAdapter trashNewsAdapter = new TrashNewsAdapter(getBaseContext(), R.layout.item_trash_new_rv, list.getNewslist());
                    ListView listView = (ListView) findViewById(R.id.rv_news);
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
                Message message = handler.obtainMessage();
                message.obj = result;
                message.what = 1;
                handler.sendMessage(message);
                Log.d("TTTT", "这里是发送消息的线程，发送的内容是：" + result + "  线程名是：" + Thread.currentThread().getName());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("thread", e.toString());
            }
        }
    }
}
