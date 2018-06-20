package com.zhketech.client.app.sip.page;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhketech.client.app.sip.R;
import com.zhketech.client.app.sip.ResolveRtsp;
import com.zhketech.client.app.sip.basepage.BaseActivity;
import com.zhketech.client.app.sip.beans.DeviceInfor;
import com.zhketech.client.app.sip.global.AppConfig;
import com.zhketech.client.app.sip.utils.Logutils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Root on 2018/6/19.
 */

public class Main  extends BaseActivity implements View.OnClickListener {



    @Override
    public int intiLayout() {
        return R.layout.main_activity;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.button_intercom:
                DeviceInfor deviceInfor = new DeviceInfor();
                deviceInfor.setUsername("admin");
                deviceInfor.setPassword("pass");
                deviceInfor.setServiceURL("http://19.0.0.213/onvif/device_service");
                ResolveRtsp resolveRtsp = new ResolveRtsp(deviceInfor);
                resolveRtsp.start();

                break;
            case R.id.button_phone:

                break;
            case R.id.button_setup:
                intent.setClass(Main.this,SettingActivity.class);
                startActivity(intent);

                break;
            case R.id.button_video:
                break;
            case R.id.button_alarm://跳转到报警页面

                break;
            case R.id.button_applyforplay:  //申请供弹

                break;
        }

    }
}
