package com.example.trashclassify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trashclassify.tools.listShow.DetailActivity;
import com.example.trashclassify.tools.listShow.Garbage_adapter;
import com.example.trashclassify.tools.model.TrashResponse;
import com.example.trashclassify.tools.model.TrashTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassifyTest extends AppCompatActivity {

    TrashTest list = new TrashTest();
    TrashTest.NewslistBean question;
    String result;
    private Handler handler;
    private Handler mHandler;
    String content;
    private int get = 0;
    private int tot = 0;
    Button button;
    Button btnA;
    Button btnB;
    Button btnC;
    Button btnD;
    Button btn_over;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_test);
        button = findViewById(R.id.btn_next);
        btn_over = findViewById(R.id.btn_over);
        btnA = findViewById(R.id.btn_a);
        btnB = findViewById(R.id.btn_b);
        btnC = findViewById(R.id.btn_c);
        btnD = findViewById(R.id.btn_d);
        mHandler = new MyHandler();
        handler = new MyHandler();
        Message message = mHandler.obtainMessage();
        message.obj = "";
        message.what = 2;
        mHandler.sendMessage(message);
        btn_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ClassifyTest.this, "一共答题" + tot + "\n" + "累计正确" + get,Toast.LENGTH_LONG).show();
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = mHandler.obtainMessage();
                message.obj = "";
                message.what = 2;
                mHandler.sendMessage(message);
            }
        });
        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(0);
            }
        });
        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(1);
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(2);
            }
        });
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(3);
            }
        });
        //Log.i("json", "num="+questionList.size());
    }

    private void check(int t) {
        ++tot;
        if (question.getTypeInt() == t) {
            Toast.makeText(ClassifyTest.this, "正确",Toast.LENGTH_SHORT).show();
            ++get;
        }
        else {
            Toast.makeText(ClassifyTest.this, "回答错误，正确答案是:" + question.getType(),Toast.LENGTH_SHORT).show();
            content = question.getName();
            Message message = mHandler.obtainMessage();
            message.obj = content;
            message.what = 3;
            mHandler.sendMessage(message);
        }
    }

    class SearchThread extends Thread{

        @Override
        public void run() {
            super.run();
            BufferedReader reader = null;
            StringBuffer sbf = new StringBuffer();
            String httpUrl = "https://api.tianapi.com/lajifenlei/index?key=601465edd44462e940c17988d0250e21&word=" + content + "&mode=1";
            try {
                //Thread.sleep(2 * 1000);
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sbf.append(strRead);
                    sbf.append("\r\n");
                    //Log.e("thread", "success!!!");
                }
                reader.close();
                result = sbf.toString();
                Message message = handler.obtainMessage();
                message.obj = result;
                message.what = 4;
                handler.sendMessage(message);
                //Log.d("TTTT", "发送success!!!");
            } catch (Exception e) {
                e.printStackTrace();
                //Log.e("thread", e.toString());
            }
        }
    }

    class MyThread extends Thread{

        @Override
        public void run() {
            super.run();
            BufferedReader reader = null;
            StringBuffer sbf = new StringBuffer();
            String httpUrl = "http://api.tianapi.com/anslajifenlei/index?key=601465edd44462e940c17988d0250e21&word=";
            try {
                //Thread.sleep(2 * 1000);
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sbf.append(strRead);
                    sbf.append("\r\n");
                    //Log.e("thread", "success!!!");
                }
                reader.close();
                result = sbf.toString();
                Message message = handler.obtainMessage();
                message.obj = result;
                message.what = 1;
                handler.sendMessage(message);
                //Log.d("TTTT", "发送success!!!");
            } catch (Exception e) {
                e.printStackTrace();
                //Log.e("thread", e.toString());
            }
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //String str = (String) msg.obj;
                    //Log.d("TTTT", result + "  线程名是：" + Thread.currentThread().getName());
                    list.setList(result);
                    String listName = list.getNewslistName();
                    //questionList.add(list.getNewslist().get(0));
                    Log.i("json", listName);
                    question = list.getNewslist();
                    TextView questionText = findViewById(R.id.question_text);
                    questionText.setText(question.getName() + "属于哪一类？");
                    break;
                case 2:
                    result = "";
                    Thread t = new MyThread();
                    t.start();
                    break;
                case 3:
                    content = (String) msg.obj;
                    Thread thread = new SearchThread();
                    thread.start();
                    break;
                case 4:
                    TrashResponse Rlist = new TrashResponse();
                    Rlist.setList(result);
                    TrashResponse.NewslistBean re = Rlist.getNewslist().get(0);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", re.getName());
                    bundle.putString("type", re.getType());
                    bundle.putInt("aipre", re.getAipre());
                    bundle.putString("explain", re.getExplain());
                    bundle.putString("contain", re.getContain());
                    bundle.putString("tip", re.getTip());
                    Intent intent=new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(ClassifyTest.this, DetailActivity.class);
                    startActivity(intent);
                    break;
                default:
                    //Log.e("thread", "error!!!! because not put content");
                    break;
            }

        }
    }
}