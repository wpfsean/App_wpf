package com.zhketech.client.app.sip.page;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.zhketech.client.app.sip.R;
import com.zhketech.client.app.sip.basepage.BaseActivity;
import com.zhketech.client.app.sip.global.AppConfig;
import com.zhketech.client.app.sip.phone.tils.Linphone;
import com.zhketech.client.app.sip.phone.tils.PhoneCallback;
import com.zhketech.client.app.sip.phone.tils.RegistrationCallback;
import com.zhketech.client.app.sip.phone.tils.SipService;
import com.zhketech.client.app.sip.services.SendheartService;
import com.zhketech.client.app.sip.utils.Logutils;
import com.zhketech.client.app.sip.utils.SharedPreferencesUtils;

import org.linphone.core.LinphoneCall;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Root on 2018/6/19.
 *
 * 主页面（6个按钮）
 */

public class Main  extends BaseActivity implements View.OnClickListener {

    Context context;

    @Override
    public int intiLayout() {
        return R.layout.main_activity;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        context = this;

    }

    @Override
    public void initData() {
        startService(new Intent(this, SendheartService.class));
        configureBlueTooth();
        loginToSipServer();

    }

    //配置蓝牙功能
    private void configureBlueTooth() {
        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 2000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);


    }

    @OnClick(R.id.button_intercom)
    public void goIntercomPage(View view){

        Intent intent = new Intent();
        intent.setClass(Main.this,SipGroup.class);
        Main.this.startActivity(intent);

    }

    private void loginToSipServer() {
        final String sipServer = (String) SharedPreferencesUtils.getObject(context,"sip_server_ip","");
        final String sipName = (String) SharedPreferencesUtils.getObject(context,"sip_name","");
        final String sipPass = (String) SharedPreferencesUtils.getObject(context,"sip_pass","");

        if (!TextUtils.isEmpty(sipServer) && !TextUtils.isEmpty(sipName) && !TextUtils.isEmpty(sipPass) ){
            if (!SipService.isReady()) {
                Linphone.startService(context);
                loginServer(sipName,sipPass,sipServer);
            }
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastShort("未获取注册sip时的信息");
                }
            });
            loginServer(sipName,sipPass,sipServer);
        }
    }

    private void loginServer(String sipName, String sipPass, String sipServer) {
        Linphone.setAccount(sipName, sipPass, sipServer);
        Linphone.login();
    }
    @Override
    public void onClick(View v) {

    }


    @OnClick(R.id.button_applyforplay)
    public void buleToothOpenBox(View view){

        //蓝牙弹箱


       boolean isOpenBlueTooth = BleManager.getInstance().isBlueEnable();
       if (!isOpenBlueTooth){
           BleManager.getInstance().enableBluetooth();
       }

       BleManager.getInstance().connect(AppConfig.blueToothMac, new BleGattCallback() {
           @Override
           public void onStartConnect() {

           }

           @Override
           public void onConnectFail(BleDevice bleDevice, final BleException e) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       toastShort("连接失败："+e.getDescription());
                   }
               });
           }

           @Override
           public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       toastShort("连接成功："+bleDevice.getName()+bleDevice.getMac());
                   }
               });
           }

           @Override
           public void onDisConnected(boolean b, BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {

           }
       });



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

    @Override
    protected void onResume() {
        super.onResume();

        //注册监听
        Linphone.addCallback(new RegistrationCallback() {
            @Override
            public void registrationNone() {
                super.registrationNone();
            }

            @Override
            public void registrationProgress() {
                super.registrationProgress();
            }

            @Override
            public void registrationOk() {
                super.registrationOk();
                Logutils.i("Ok");
            }

            @Override
            public void registrationCleared() {
                super.registrationCleared();
            }

            @Override
            public void registrationFailed() {
                super.registrationFailed();
                Logutils.i("fail");
                //掉线时重新注册
                loginToSipServer();
            }
        }, new PhoneCallback() {
            @Override
            public void incomingCall(LinphoneCall linphoneCall) {
                super.incomingCall(linphoneCall);

                Logutils.i("来电");
            }

            @Override
            public void outgoingInit() {
                super.outgoingInit();
                Logutils.i("打电话");
            }

            @Override
            public void callConnected() {
                super.callConnected();
                Logutils.i("电话挂断");
            }

            @Override
            public void callEnd() {
                super.callEnd();
                Logutils.i("电话结束");
            }

            @Override
            public void callReleased() {
                super.callReleased();
                Logutils.i("电话释放");
            }

            @Override
            public void error() {
                super.error();
                Logutils.i("电话error");
            }
        });
    }
}
