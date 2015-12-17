package com.xiaoxuan.news.model.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xiaoxuan.news.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDBManage {
    private UserDBHelper helper;
    private SQLiteDatabase db;

    public UserDBManage(Context context) {
        helper = new UserDBHelper(context);
        db = helper.getWritableDatabase();
    }

    //插入公交名称
    public boolean add(User user) {
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO User VALUES(?,?)"
                    , new String[]{user.getPhoneNumber(), user.getPassword()});
            db.setTransactionSuccessful();//设置事务成功完成
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            db.endTransaction();//结束事务
        }
    }

    //查询所有公交信息
    public List<User> query() {
        List<User> users= new ArrayList<>();
        Cursor c = queryAllCursor();
        while (c.moveToNext()) {
            User user= new User();
            user.setPhoneNumber(c.getString(c.getColumnIndex("phoneNumber")));
            user.setPassword(c.getString(c.getColumnIndex("password")));
            users.add(user);
        }
        c.close();//关闭游标
        return users;
    }

    //删除
    public void deleteAll(List<User> userList ) {
        for (User user : userList) {
            db.delete("User", "phoneNumber = ?", new String[]{user.getPhoneNumber()});//删除所有数据
        }

    }

    //查询所有
    public Cursor queryAllCursor() {
        Cursor c = db.rawQuery("SELECT * FROM User", null);//查表
        return c;
    }

    //关闭数据库
    public void closeDB() {
        db.close();
    }

}
