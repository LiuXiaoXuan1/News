package com.xiaoxuan.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xiaoxuan.news.R;
import com.xiaoxuan.news.model.DB.UserDBManage;
import com.xiaoxuan.news.model.entity.News;

import java.util.List;

/**
 * Created by cc on 2015/12/16 0016.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    private int resourceId;

    public NewsAdapter(Context context, int resourceId, List<News> newsList) {
        super(context, resourceId, newsList);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.news_title);
        textView.setText(news.getTitle());
        return view;
    }
}
