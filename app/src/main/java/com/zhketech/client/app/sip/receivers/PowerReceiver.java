package com.zhketech.client.app.sip.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import com.zhketech.client.app.sip.utils.Logutils;

/**
 * Created by Root on 2018/6/21.
 * 电量监听
 */

public class PowerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
        int levelPercent = (int) (((float) level / scale) * 100);
        Logutils.i("level:" + levelPercent);
    }
}
