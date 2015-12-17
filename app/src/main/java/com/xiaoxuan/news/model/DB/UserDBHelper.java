package com.xiaoxuan.news.model.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHelper extends SQLiteOpenHelper {

    public UserDBHelper(Context context) {//必须声明构造函数
        super(context, "news_db", null, 1);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS User" +
                "(phoneNumber TEXT NOT NULL UNIQUE, password TEXT)");
    }

    //必须重载此方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE User ADD COLUMN other STRING");
    }

}
