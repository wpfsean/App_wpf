package com.zhketech.client.app.sip.page;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Toast;

import com.zhketech.client.app.sip.R;
import com.zhketech.client.app.sip.basepage.BaseActivity;
import com.zhketech.client.app.sip.beans.LoginBean;
import com.zhketech.client.app.sip.utils.LoginThread;
import com.zhketech.client.app.sip.utils.Logutils;
import com.zhketech.client.app.sip.utils.PhoneUtils;
import com.zhketech.client.app.sip.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_username_layout)
    EditText username;
    @BindView(R.id.edit_userpass_layout)
    EditText userpass;
    @BindView(R.id.remember_pass_layout)
    Checkable remeemberPass;
    @BindView(R.id.auto_login_layout)
    Checkable autoLogin;

    boolean isRemember;
    boolean isAuto;
    String native_ip;


    @Override
    public int intiLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        native_ip = PhoneUtils.displayIpAddress(this);

        String logined_name = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "username", "");
        String logined_pass = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "userpass", "");
        //输入框显示用户名和密码
        username.setText(logined_name);
        userpass.setText(logined_pass);
        //判断是否是自动 登录，如果是自动登录就直接跳转到主页面
        boolean isAutoLogin = (boolean) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "auto", false);
        if (isAutoLogin == true) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, Main.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    @OnClick(R.id.userlogin_button_layout)
    public void login(View view) {

        final String name = username.getText().toString().trim();
        final String pass = userpass.getText().toString().trim();
        isRemember = remeemberPass.isChecked();
        isAuto = autoLogin.isChecked();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {
            LoginBean loginBean = new LoginBean();
            loginBean.setUsername(name);
            loginBean.setPass(pass);
            if (!TextUtils.isEmpty(native_ip)) {
                loginBean.setIp(name);
            } else {
                loginBean.setIp("19.0.0.79");
            }
            LoginThread loginThread = new LoginThread(LoginActivity.this, loginBean, new LoginThread.IsLoginListern() {
                @Override
                public void loginStatus(String status) {
                    String result = status;
                    if (!TextUtils.isEmpty(result)) {
                        if (result.equals("success")) {
                            //如果复选了就存储相关的信息
                            if (isRemember == true) {
                                SharedPreferencesUtils.putObject(ZkthApp.getInstance(), "username", name);
                                SharedPreferencesUtils.putObject(ZkthApp.getInstance(), "userpass", pass);
                            }
                            if (isAuto) {
                                SharedPreferencesUtils.putObject(ZkthApp.getInstance(), "auto", true);
                            }
                            try {
                                Thread.sleep(2 * 1000);
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, Main.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            } catch (InterruptedException e) {
                                Logutils.e("Login error");
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toastShort("未知原因，登陆失败!!!");
                                }
                            });
                        }
                    }
                }
            });
            loginThread.start();
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastShort("EditText null");
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

