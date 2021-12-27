package com.example.trashclassify.tools.listShow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trashclassify.R;
import com.example.trashclassify.tools.model.TrashNewsResponse;

import java.util.List;

public class TrashNewsAdapter extends ArrayAdapter {
    private int resourceId;

    public TrashNewsAdapter(Context context, int textViewResourceId, List<TrashNewsResponse.NewslistBean> objects) {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrashNewsResponse.NewslistBean newsList = (TrashNewsResponse.NewslistBean) getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
        TextView newsTitle=(TextView)view.findViewById(R.id.tv_title);
        TextView newsDescription=(TextView)view.findViewById(R.id.tv_description);
        newsTitle.setText(newsList.getTitle());
        newsDescription.setText(newsList.getDescription());
        return view;
    }
}
