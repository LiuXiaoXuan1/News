package com.xiaoxuan.news.http;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.pingplusplus.android.PaymentActivity;
import com.xiaoxuan.news.common.Common;
import com.xiaoxuan.news.model.entity.PaymentRequest;
import com.xiaoxuan.news.util.Tools;

import java.util.Map;

/**
 * Created by chanplion on 2015/12/18 0018.
 */
public class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

    private Context context;

    private Map<String, View> viewMap;
    /**
     *开发者需要填一个服务端URL 该URL是用来请求支付需要的charge。务必确保，URL能返回json格式的charge对象。
     *服务端生成charge 的方式可以参考ping++官方文档，地址 https://pingxx.com/guidance/server/import
     *
     *【 http://218.244.151.190/demo/charge 】是 ping++ 为了方便开发者体验 sdk 而提供的一个临时 url 。
     * 改 url 仅能调用【模拟支付控件】，开发者需要改为自己服务端的 url 。
     */
    public static final String URL = "http://218.244.151.190/demo/charge";


    public PaymentTask(Context context, Map<String, View> viewMap) {
        this.viewMap = viewMap;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        //按键点击之后的禁用，防止重复点击
        ((RelativeLayout) viewMap.get("rltWechat")).setOnClickListener(null);
        ((RelativeLayout) viewMap.get("rltZhifubao")).setOnClickListener(null);
    }

    @Override
    protected String doInBackground(PaymentRequest... pr) {

        PaymentRequest paymentRequest = pr[0];
        String data = null;
        String json = new Gson().toJson(paymentRequest);
        try {
            //向Your Ping++ Server SDK请求数据
            data = Tools.postJson(URL, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获得服务端的charge，调用ping++ sdk。
     */
    @Override
    protected void onPostExecute(String data) {
        if(null==data) {
            Tools.ShowToast(context, "请求出错, 请检查URL, URL无法获取charge");
            return;
        }
        Intent intent = new Intent();
        String packageName = context.getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
        AppCompatActivity appCompatActivity = new AppCompatActivity();
        appCompatActivity = (AppCompatActivity) context;//强制转化成PayActivity
        appCompatActivity.startActivityForResult(intent, Common.REQUEST_CODE_PAYMENT);

    }

}
