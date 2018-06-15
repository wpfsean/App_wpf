package com.zhketech.client.app.sip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.zhketech.client.app.sip.sip.tils.Linphone;
import com.zhketech.client.app.sip.sip.tils.SipService;
import com.zhketech.client.app.sip.sip.tils.RegistrationCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.sip_account)
    EditText mAccount;
    @BindView(R.id.sip_password)
    EditText mPassword;
    @BindView(R.id.sip_server)
    EditText mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (!SipService.isReady()) {
            Linphone.startService(this);
            Linphone.addCallback(new RegistrationCallback() {
                @Override
                public void registrationOk() {
                    super.registrationOk();
                    Log.e(TAG, "registrationOk: ");
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                }

                @Override
                public void registrationFailed() {
                    super.registrationFailed();
                    Log.e(TAG, "registrationFailed: ");
                    Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } else {
            goToMainActivity();
        }
    }

    @OnClick(R.id.press_login)
    public void login() {
        String account = mAccount.getText().toString();
        String password = mPassword.getText().toString();
        String serverIP = mServer.getText().toString();
        Linphone.setAccount(account, password, serverIP);
        Linphone.login();
    }

    private void goToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
