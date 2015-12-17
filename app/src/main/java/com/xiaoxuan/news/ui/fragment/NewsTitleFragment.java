package com.xiaoxuan.news.ui.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiaoxuan.news.R;
import com.xiaoxuan.news.adapter.NewsAdapter;
import com.xiaoxuan.news.model.DB.UserDBManage;
import com.xiaoxuan.news.model.entity.News;
import com.xiaoxuan.news.ui.activity.NewsContentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cc on 2015/12/16 0016.
 */
public class NewsTitleFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;

    private List<News> newses;

    private NewsAdapter adapter;

    private boolean isTwoPane;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        newses = getNews();//初始化新闻数据
        adapter = new NewsAdapter(activity, R.layout.news_item, newses);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title_frag, container, false);
        listView = (ListView) view.findViewById(R.id.news_title_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.news_content_layout) != null) {//可以找到双页模式
            isTwoPane = true;
        } else {
            isTwoPane = false;//不能找到双页模式
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = newses.get(position);
        if (isTwoPane) {
            NewsContentFragment fragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
            fragment.refresh(news.getTitle(), news.getContent());
        } else {
            NewsContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());
        }
    }

    private List<News> getNews() {
        List<News> newsList = new ArrayList<>();
        News news1 = new News();
        news1.setTitle("Succeed in College as a Learning Disabled Student");
        news1.setContent("College freshmen will soon learn to live with a roomate.");
        newsList.add(news1);
        News news2 = new News();
        news2.setTitle("Google Android exec poached by China's Xiaomi");
        news2.setContent("China's Xiaomi has poached a key Google executive involved in the tech giant't Android phones..");
        newsList.add(news2);
        return newsList;
    }
}
