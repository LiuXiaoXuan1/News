package com.xiaoxuan.news.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xiaoxuan.news.R;
import com.xiaoxuan.news.ui.fragment.NewsContentFragment;

/**
 * Created by cc on 2015/12/16 0016.
 */
public class NewsContentActivity extends AppCompatActivity {

    public static void actionStart(Context context, String title, String content) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("new_title", title);
        intent.putExtra("new_content", content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);

        initView();

    }

    private void initView() {
        String title = getIntent().getStringExtra("new_title");
        String content = getIntent().getStringExtra("new_content");
        NewsContentFragment fragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
        fragment.refresh(title, content);//刷新界面
    }

}
