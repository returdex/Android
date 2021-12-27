package com.example.trashclassify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.trashclassify.tools.KylinSearchView;
import com.example.trashclassify.tools.listShow.DetailActivity;
import com.example.trashclassify.tools.listShow.Garbage_adapter;
import com.example.trashclassify.tools.model.TrashResponse;
import com.example.trashclassify.tools.search.OnSearchListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Search extends AppCompatActivity implements OnSearchListener {
    private KylinSearchView searchView;
    private ListView garbageList_View;
    TrashResponse list = new TrashResponse();
    String result;
    private Handler handler;
    private Handler mHandler;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (KylinSearchView) findViewById(R.id.sv_default);
        searchView.setOnSearchListener(Search.this);
        garbageList_View=findViewById(R.id.search_listview);
        garbageList_View.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("garbage_id", i);
                List<TrashResponse.NewslistBean> nList = list.getNewslist();
                bundle.putString("name", nList.get(i).getName());
                bundle.putString("type", nList.get(i).getType());
                bundle.putInt("aipre", nList.get(i).getAipre());
                bundle.putString("explain", nList.get(i).getExplain());
                bundle.putString("contain", nList.get(i).getContain());
                bundle.putString("tip", nList.get(i).getTip());
                Intent intent=new Intent();
                intent.putExtras(bundle);
                intent.setClass(Search.this, DetailActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void search(String content) {
        mHandler = new MyHandler();
        handler = new MyHandler();
        Message message = mHandler.obtainMessage();
        message.obj = content;
        message.what = 2;
        mHandler.sendMessage(message);

    }

    class MyThread extends Thread{

        @Override
        public void run() {
            super.run();
            BufferedReader reader = null;
            StringBuffer sbf = new StringBuffer();
            String httpUrl = "https://api.tianapi.com/lajifenlei/index?key=601465edd44462e940c17988d0250e21&word=" + content;
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
                    String listName = list.getNewslistName().toString();
                    //Log.i("json", listName);
                    Garbage_adapter adapter = new Garbage_adapter(Search.this, R.layout.garbage_item, list.getNewslist());
                    ListView listView=(ListView) findViewById(R.id.search_listview);
                    listView.setAdapter(adapter);
                    break;
                case 2:
                    content = (String) msg.obj;
                    list.clear();
                    Thread t = new MyThread();
                    t.start();
                    break;
                default:
                    //Log.e("thread", "error!!!! because not put content");
                    break;
            }

        }
    }
}