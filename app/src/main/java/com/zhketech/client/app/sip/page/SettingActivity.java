package com.zhketech.client.app.sip.page;

import android.content.pm.ActivityInfo;
import android.view.View;

import com.zhketech.client.app.sip.R;
import com.zhketech.client.app.sip.basepage.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Root on 2018/6/19.
 */

public class SettingActivity extends BaseActivity {
    @Override
    public int intiLayout() {
        return R.layout.activity_setting_center;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.setting_center_button)
    public void setDirection(View view) {

        int direction = ZkthApp.getInstance().direction;
        if (direction == 1){
            ZkthApp.getInstance().setDirection(2);
        }else if (direction == 2){
            ZkthApp.getInstance().setDirection(1);
        }
        restartApplication();
    }


}
