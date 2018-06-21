package com.zhketech.client.app.sip.page;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.zhketech.client.app.sip.R;
import com.zhketech.client.app.sip.basepage.BaseActivity;
import com.zhketech.client.app.sip.beans.DeviceInfor;
import com.zhketech.client.app.sip.beans.LoginParameters;
import com.zhketech.client.app.sip.beans.SipBean;
import com.zhketech.client.app.sip.beans.VideoBen;
import com.zhketech.client.app.sip.callbacks.RequestSipSourcesThread;
import com.zhketech.client.app.sip.callbacks.RequestVideoSourcesThread;
import com.zhketech.client.app.sip.utils.LoginToService;
import com.zhketech.client.app.sip.utils.Logutils;
import com.zhketech.client.app.sip.utils.PhoneUtils;
import com.zhketech.client.app.sip.utils.ResolveRtsp;
import com.zhketech.client.app.sip.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    //整个项目可能用到的权限
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.USE_SIP,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE
    };
    //存放未同意 的权限
    List<String> mPermissionList = new ArrayList<>();


    @BindView(R.id.edit_username_layout)
    EditText username;
    @BindView(R.id.edit_userpass_layout)
    EditText userpass;
    @BindView(R.id.remember_pass_layout)
    Checkable remeemberPass;
    @BindView(R.id.auto_login_layout)
    Checkable autoLogin;

    @BindView(R.id.edit_serviceip_layout)
    EditText serverip;

    @BindView(R.id.remembe_serverip_layout)
    CheckBox serverip_ck;

    boolean isRemember;
    boolean isAuto;
    String native_ip;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //申请权限
            requestPermission();
        } else {
            //不用申请权限，加载数据
            initPageData();
        }
    }

    private void requestPermission() {
        mPermissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        } /** * 判断存储委授予权限的集合是否为空 */
        if (!mPermissionList.isEmpty()) {
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(LoginActivity.this, permissions, 1);
        } else {
            //未授予的权限为空，表示都授予了 // 后续操作...
            initPageData();
        }
    }

    boolean mShowRequestPermission = true;//用户是否禁止权限

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissions[i]);
                        if (showRequestPermission) {//
                            requestPermission();//重新申请权限
                            return;
                        } else {
                            mShowRequestPermission = false;//已经禁止
                            String permisson = permissions[i];
                            android.util.Log.i("TAG", "permisson:" + permisson);
                        }
                    }
                }
                initPageData();
                break;
            default:
                break;
        }
    }


    private void initPageData() {


        native_ip = PhoneUtils.displayIpAddress(this);
        String logined_name = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "username", "");
        String logined_pass = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "userpass", "");
        String logined_serverip = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "serverip", "");
        boolean p = (boolean) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "rememberinfor", false);
        //输入框显示用户名和密码
        username.setText(logined_name);
        userpass.setText(logined_pass);
        serverip.setText(logined_serverip);
        if (p) serverip.setEnabled(false);

        serverip_ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serverip.setEnabled(true);
                } else {
                    serverip.setEnabled(false);
                }
            }
        });
        //判断是否是自动 登录，如果是自动登录就直接跳转到主页面
        boolean isAutoLogin = (boolean) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "auto", false);
        if (isAutoLogin == true) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, Main.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    @OnClick(R.id.userlogin_button_layout)
    public void login(View view) {

        final String name = username.getText().toString().trim();
        final String pass = userpass.getText().toString().trim();
        final String server_IP = serverip.getText().toString().trim();
        isRemember = remeemberPass.isChecked();
        isAuto = autoLogin.isChecked();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(server_IP)) {
            LoginParameters loginBean = new LoginParameters();
            loginBean.setUsername(name);
            loginBean.setPass(pass);
            loginBean.setServer_ip(server_IP);
            if (!TextUtils.isEmpty(native_ip)) {
                loginBean.setNative_ip(native_ip);
                SharedPreferencesUtils.putObject(ZkthApp.getInstance(), "current_ip", native_ip);
            } else {
                loginBean.setNative_ip("19.0.0.79");
            }

            LoginToService loginThread = new LoginToService(LoginActivity.this, loginBean, new LoginToService.IsLoginListern() {
                @Override
                public void loginStatus(String status) {
                    final String result = status;
                    if (!TextUtils.isEmpty(result)) {
                        if (result.equals("success")) {
                            //如果复选了就存储相关的信息
                            if (isRemember == true) {
                                SharedPreferencesUtils.putObject(ZkthApp.getInstance(), "rememberinfor", true);
                                SharedPreferencesUtils.putObject(ZkthApp.getInstance(), "serverip", server_IP);
                                SharedPreferencesUtils.putObject(ZkthApp.getInstance(), "username", name);
                                SharedPreferencesUtils.putObject(ZkthApp.getInstance(), "userpass", pass);
                            }
                            if (isAuto) {
                                SharedPreferencesUtils.putObject(ZkthApp.getInstance(), "auto", true);
                            }
//                            Intent intent1 = new Intent(LoginActivity.this, ReceiverMessageService.class);
//                            startService(intent1);

                            try {
                                Thread.sleep(1 * 1000);
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, Main.class);
                                startActivity(intent);
                                obtainDataCMS();
                                LoginActivity.this.finish();
                            } catch (InterruptedException e) {
                                Logutils.e("Login error");
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toastShort("未知原因，登陆失败!!!\n" + result);
                                }
                            });
                        }
                    }
                }
            });
            loginThread.start();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastShort("EditText null");
                }
            });
        }
    }

    private void obtainDataCMS() {

        RequestVideoSourcesThread requestVideoSourcesThread = new RequestVideoSourcesThread(LoginActivity.this, new RequestVideoSourcesThread.GetDataListener() {
            @Override
            public void getResult(List<VideoBen> devices) {
                if (devices != null && devices.size() > 0)
                    resolverRTSP(devices);
                else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastShort("未获取到视频资源");
                        }
                    });
            }
        });
        requestVideoSourcesThread.start();

        RequestSipSourcesThread requestSipSourcesThread = new RequestSipSourcesThread(LoginActivity.this, "0", new RequestSipSourcesThread.SipListern() {
            @Override
            public void getDataListern(List<SipBean> mList) {

                Logutils.i("mlist:"+mList.toString());

            }
        });
        requestSipSourcesThread.start();
    }


    private synchronized void  resolverRTSP(List<VideoBen> devices) {

        for (int i=0;i<devices.size();i++){
            if (devices.get(i).getDevicetype().equals("ONVIF")) {
                DeviceInfor deviceInfor = new DeviceInfor();
                deviceInfor.setUsername(devices.get(i).getUsername());
                deviceInfor.setPassword(devices.get(i).getPassword());
                deviceInfor.setChannel(devices.get(i).getChannel());
                deviceInfor.setServiceURL("http://" + devices.get(i).getIp() + "/onvif/device_service");
                ResolveRtsp resolveRtsp = new ResolveRtsp(deviceInfor);
                resolveRtsp.setOnHttpSoapListener(new ResolveRtsp.OnHttpSoapListener() {
                    @Override
                    public void OnHttpSoapDone(DeviceInfor camera, String uri, boolean success) {

                    }
                });
                resolveRtsp.start();
            } else if (devices.get(i).getDevicetype().equals("RTSP")) {
                String rtsp = "rtsp://" + devices.get(i).getUsername() + ":" + devices.get(i).getPassword() + "@" + devices.get(i).getIp() + ":" + devices.get(i).getPort() + "/" + devices.get(i).getChannel();
               // Logutils.i("rtsp:"+rtsp);
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}

