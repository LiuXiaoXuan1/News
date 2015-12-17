package com.xiaoxuan.news.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.xiaoxuan.news.R;
import com.xiaoxuan.news.model.entity.User;
import com.xiaoxuan.news.model.DB.UserDBManage;
import com.xiaoxuan.news.util.Tools;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterPwdActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.edt_confirm_password)
    EditText edtConfirmPassword;
    @Bind(R.id.llt_finish)
    LinearLayout lltFinish;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;

    private String numberPhone = null;

    private UserDBManage dbManage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pwd);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbManage.closeDB();//关闭数据库
    }

    private void initView() {
        lltFinish.setOnClickListener(this);
        Intent intent = this.getIntent();
        numberPhone = intent.getStringExtra("numberPhone");
        dbManage = new UserDBManage(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llt_finish:
                String password = edtPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();
                if (password.length() != 0 && confirmPassword.length() != 0) {
                    progressbar.setVisibility(View.VISIBLE);
                    int i = 0;
                    List<User> userList = dbManage.query();
                    if (password.equals(confirmPassword)) {
                        progressbar.setVisibility(View.GONE);
                        for (; i < userList.size(); i++) {
                            if (numberPhone.equals(userList.get(i).getPhoneNumber())) {
                                Tools.ShowToast(this, "此用户已注册过");
                                break;
                            }
                        }
                        if (i == userList.size()) {//该用户如果没注册过则插入本地
                            User user = new User();
                            user.setPhoneNumber(numberPhone);
                            user.setPassword(password);
                            dbManage.add(user);//此用户注册成功，存储到本地数据库上
                            Intent intent = new Intent(RegisterPwdActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        progressbar.setVisibility(View.GONE);
                        Tools.ShowToast(this, "两个密码不一致，请重新填写");
                    }
                } else {
                    Tools.ShowToast(this, "请填写完整的密码信息~");
                }
                break;
        }
    }

}
