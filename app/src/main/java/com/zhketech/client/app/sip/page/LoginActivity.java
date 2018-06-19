package com.zhketech.client.app.sip.page;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zhketech.client.app.sip.R;
import com.zhketech.client.app.sip.basepage.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv)
    TextView tv;
    int num;


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
        ZkthApp.getInstance().addActivity(this);

         num = ZkthApp.getInstance().direction;
        Log.i("TAG",num+"///");

    }

    @OnClick(R.id.custom_direction)
    public void customDirection(View view){


        if (num == 1){
            ZkthApp.getInstance().setDirection(2);
        }else  if (num == 2){
            ZkthApp.getInstance().setDirection(1);
        }

        restartApplication();
    }



}

