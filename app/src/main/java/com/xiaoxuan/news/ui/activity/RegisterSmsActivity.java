package com.xiaoxuan.news.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaoxuan.news.R;
import com.xiaoxuan.news.util.Tools;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterSmsActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.edt_phone_number)
    EditText edtPhoneNumber;
    @Bind(R.id.edt_check)
    EditText edtCheck;
    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.tv_finish)
    TextView tvFinish;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;

    private int i = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sms);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    private void initView() {
        tvSend.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        initSMS();
    }

    @Override
    public void onClick(View v) {
        String phoneNum = edtPhoneNumber.getText().toString();
        String CheckCode = edtCheck.getText().toString();
        switch (v.getId()) {
            case R.id.tv_send:
                if (!judgePhoneNums(phoneNum)) {
                    return;
                } // 2. 通过sdk发送短信验证
                if (phoneNum != "") {
                    SMSSDK.getVerificationCode("86", phoneNum);
                } else {
                    Tools.ShowToast(RegisterSmsActivity.this,"手机号不能为空");
                }
                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                tvSend.setClickable(false);
                tvSend.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            case R.id.tv_finish:
                if (phoneNum.length() != 0 && CheckCode.length() != 0) {
                    progressbar.setVisibility(View.VISIBLE);
                    SMSSDK.submitVerificationCode("86", phoneNum, CheckCode);
                } else {
                    Tools.ShowToast(RegisterSmsActivity.this, "手机号或验证码不能空");
                }
                break;
        }
    }

    private void initSMS() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    /**
     *
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                tvSend.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                tvSend.setText("获取验证码");
                tvSend.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        progressbar.setVisibility(View.GONE);
                        Tools.ShowToast(RegisterSmsActivity.this, "提交验证码成功");
                        Intent intent = new Intent(RegisterSmsActivity.this,
                                RegisterPwdActivity.class);
                        intent.putExtra("numberPhone", edtPhoneNumber.getText().toString());
                        startActivity(intent);
                        finish();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Tools.ShowToast(RegisterSmsActivity.this, "验证码已经发送");
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Tools.ShowToast(this, "手机号码输入有误！");
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }



}
