package com.example.trashclassify.tools.listShow;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trashclassify.MainActivity;
import com.example.trashclassify.R;

import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    private TextView garbage_name;
    private TextView garbage_type;
    private TextView garbage_explain;
    private TextView garbage_contain;
    private TextView garbage_tips;
    private JSONObject search_result;
    private int detail_garbage_id;
    private String[] garbage_type_string={"可回收垃圾","有害垃圾","厨余垃圾","其它垃圾（干垃圾）"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
        Bundle bundle = getIntent().getExtras();
        garbage_name=findViewById(R.id.detail_garbage_name);
        garbage_type=findViewById(R.id.detail_garbage_type);
        garbage_explain=findViewById(R.id.detail_garbage_explain);
        garbage_contain=findViewById(R.id.detail_garbage_contain);
        garbage_tips=findViewById(R.id.detail_garbage_tips);

        garbage_name.setText(bundle.getString("name"));
        garbage_type.setText(bundle.getString("typs"));
        garbage_explain.setText(bundle.getString("explain"));
        garbage_tips.setText(bundle.getString("tip"));
        garbage_contain.setText(bundle.getString("contain"));
    }
}

