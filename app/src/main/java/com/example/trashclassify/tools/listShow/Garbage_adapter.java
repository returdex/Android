package com.example.trashclassify.tools.listShow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.trashclassify.R;
import com.example.trashclassify.tools.model.TrashResponse;

public class Garbage_adapter extends ArrayAdapter {
    private int resourceId;

    public Garbage_adapter(Context context, int textViewResourceId, List<TrashResponse.NewslistBean> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TrashResponse.NewslistBean garbage_list= (TrashResponse.NewslistBean) getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
        TextView garbageName=(TextView)view.findViewById(R.id.garbage_name);
        TextView garbageType=(TextView)view.findViewById(R.id.garbage_type);
        garbageName.setText(garbage_list.getName());
        garbageType.setText(garbage_list.getType());
        return view;
    }
}

