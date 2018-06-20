package com.zhketech.client.app.sip.global;

import com.zhketech.client.app.sip.page.ZkthApp;
import com.zhketech.client.app.sip.utils.SharedPreferencesUtils;

/**
 * Created by Root on 2018/6/19.
 *
 * 全局的配置参数
 */

public class AppConfig {
    public AppConfig() {
        throw new UnsupportedOperationException("not't constructed");
    }
    //当前的用户名
    public static String current_user = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "username", "");
    //当前的密码
    public static String current_pass = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "userpass", "");
    //当前设备的ip
    public static String current_ip = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "current_ip", "");
    //服务器ip
    public static String server_ip = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "current_ip", "");
    //登录端口
    public  static  int server_port = 2010;
    //数据格式
    public static String dataFormat = "GB2312";



}
