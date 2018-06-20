package com.zhketech.client.app.sip.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Root on 2018/6/20.
 * 接收报警消息的servier
 */

public class ReceiverMessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
