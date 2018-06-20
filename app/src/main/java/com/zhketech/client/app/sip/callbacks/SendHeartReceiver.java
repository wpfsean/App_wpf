package com.zhketech.client.app.sip.callbacks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zhketech.client.app.sip.services.SendheartService;
import com.zhketech.client.app.sip.utils.ByteUtils;
import com.zhketech.client.app.sip.utils.Logutils;
import com.zhketech.client.app.sip.utils.PhoneUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 广播接收定时任务用于发送心跳
 * Created by Root on 2018/4/27.
 */

public class SendHeartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<String> points = new ArrayList<String>();
        String deviceId = PhoneUtils.getPhoneInfo(context, 1);
        if (!TextUtils.isEmpty(deviceId) && deviceId != null) {
            points.add(deviceId);
        } else {
            points.add("111111");
        }
        long stamp = PhoneUtils.dateStamp();
        byte[] timeByte = ByteUtils.longToBytes(stamp);
        SendHearToServerThread hearToServer = new SendHearToServerThread(points, timeByte);
        new Thread(hearToServer).start();
        Logutils.i("time:" + new Date().toString());
        Intent i = new Intent(context, SendheartService.class);
        context.startService(i);
    }
}
