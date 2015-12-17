package com.xiaoxuan.news.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaoxuan.news.R;
import com.xiaoxuan.news.model.entity.User;
import com.xiaoxuan.news.model.DB.UserDBManage;
import com.xiaoxuan.news.util.Tools;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.edt_phone_number)
    EditText edtPhoneNumber;
    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.tv_finish)
    TextView tvFinish;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;

    private Intent intent = null;
    private UserDBManage dbManage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbManage.closeDB();//关闭数据库
    }

    private void initView() {
        tvRegister.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        dbManage = new UserDBManage(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                intent = new Intent(LoginActivity.this, RegisterSmsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_finish:
                String number = edtPhoneNumber.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                Boolean isErrPwd = false;//是否密码不正确
                Boolean isExist= false;//账号是否存在
                if (number.length() != 0 && password.length() != 0) {
                    progressbar.setVisibility(View.VISIBLE);
                    List<User> users = dbManage.query();
                    for (User user : users) {
                        if (number.equals(user.getPhoneNumber()) && password.equals(user.getPassword())) {
                            progressbar.setVisibility(View.GONE);
                            edtPhoneNumber.setText("");
                            edtPassword.setText("");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            isExist = true;
                            finish();
                        }else if (number.equals(user.getPhoneNumber()) && !password.equals(user.getPassword())) {
                            progressbar.setVisibility(View.GONE);
                            isErrPwd = true;
                            isExist = true;
                            Tools.ShowToast(this, "密码不正确");//没有找到匹配度的账号
                            break;
                        }
                    }
                    if (isErrPwd == false && isExist == false) {//如果不是密码不正确就是账号不存在
                        progressbar.setVisibility(View.GONE);
                        Tools.ShowToast(this, "此账号不存在");//没有找到匹配的账号
                    }
                } else {
                    Tools.ShowToast(this,"手机号码或账号不能空");
                }
                break;
        }
    }

}
