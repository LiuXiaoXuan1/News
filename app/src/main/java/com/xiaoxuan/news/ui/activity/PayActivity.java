package com.xiaoxuan.news.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.xiaoxuan.news.R;
import com.xiaoxuan.news.common.Common;
import com.xiaoxuan.news.http.PaymentTask;
import com.xiaoxuan.news.model.entity.PaymentRequest;
import com.xiaoxuan.news.util.Tools;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chanplion on 2015/12/18 0018.
 */
public class PayActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.rlt_wechat)
    RelativeLayout rltWechat;
    @Bind(R.id.rlt_zhifubao)
    RelativeLayout rltZhifubao;

    private String mount = "0.01";//0.01元

    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";

    private Map<String, View> viewMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        rltWechat.setOnClickListener(this);
        rltZhifubao.setOnClickListener(this);
        viewMap.put("rltWechat", rltWechat);
        viewMap.put("rltZhifubao", rltZhifubao);
    }

    @Override
    public void onClick(View v) {
        String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));
        String cleanString = mount.replaceAll(replaceable, "");
        int amount = Integer.valueOf(new BigDecimal(cleanString).toString());
        switch (v.getId()) {
            case R.id.rlt_wechat:
                new PaymentTask(PayActivity.this, viewMap).execute(new PaymentRequest(CHANNEL_WECHAT, amount));
                break;
            case R.id.rlt_zhifubao:
                new PaymentTask(PayActivity.this, viewMap).execute(new PaymentRequest(CHANNEL_ALIPAY, amount));
                break;
        }
    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        rltWechat.setOnClickListener(PayActivity.this);
        rltZhifubao.setOnClickListener(PayActivity.this);

        //支付页面返回处理
        if (requestCode == Common.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                Tools.ShowToast(this, result + errorMsg + extraMsg);
            }
        }
    }

    public void callback(Intent intent) {
        startActivityForResult(intent,Common.REQUEST_CODE_PAYMENT);
    }
}
