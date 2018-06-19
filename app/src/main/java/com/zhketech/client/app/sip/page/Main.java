package com.zhketech.client.app.sip.page;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhketech.client.app.sip.R;
import com.zhketech.client.app.sip.basepage.BaseActivity;
import com.zhketech.client.app.sip.global.AppConfig;
import com.zhketech.client.app.sip.utils.Logutils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Root on 2018/6/19.
 */

public class Main  extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.main_relativeLayout)
    public RelativeLayout main_relativeLayout;

    //显示时间
    @BindView(R.id.main_incon_time)
    public TextView main_incon_time;

    //显示日期
    @BindView(R.id.main_icon_date)
    public TextView main_icon_date;

    //对讲
    @BindView(R.id.button_intercom)
    public ImageButton button_intercom;
    //电话
    @BindView(R.id.button_phone)
    public ImageButton button_phone;
    //设置
    @BindView(R.id.button_setup)
    public ImageButton button_setup;
    //视频
    @BindView(R.id.button_video)
    public ImageButton button_video;
    //报警
    @BindView(R.id.button_alarm)
    public ImageButton button_alarm;
    //申请供弹
    @BindView(R.id.button_applyforplay)
    public ImageButton button_appplyforplay;

    @Override
    public int intiLayout() {
        return R.layout.main_activity;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        button_intercom.setOnClickListener(this);
        button_phone.setOnClickListener(this);
        button_setup.setOnClickListener(this);
        button_video.setOnClickListener(this);
        button_alarm.setOnClickListener(this);
        button_appplyforplay.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.button_intercom:

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
