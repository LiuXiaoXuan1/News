package com.xiaoxuan.news.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoxuan.news.R;

/**
 * Created by cc on 2015/12/16 0016.
 */
public class NewsContentFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_content_frag, container, false);
        return view;
    }

    public void refresh(String title, String content) {
        View visibilityLayout = view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView titleText = (TextView) visibilityLayout.findViewById(R.id.news_title);
        TextView contentText = (TextView) visibilityLayout.findViewById(R.id.news_content);
        titleText.setText(title);//刷新新闻标题
        contentText.setText(content);//刷新新闻内容
    }
}
