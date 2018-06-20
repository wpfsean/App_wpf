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
import butterknife.OnClick;

/**
 * Created by Root on 2018/6/19.
 *
 * 主页面（6个按钮）
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

    }


    @OnClick(R.id.button_setup)
    public void setDirection(View view){

        int direction = ZkthApp.getInstance().direction;
        if (direction == 1){
            ZkthApp.getInstance().setDirection(2);
        }else if (direction == 2){
            ZkthApp.getInstance().setDirection(1);
        }

        restartApplication();
    }

}
