package com.xiaoxuan.news.util;

import android.content.Context;
import android.widget.Toast;

public class Tools {

    public static void ShowToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
