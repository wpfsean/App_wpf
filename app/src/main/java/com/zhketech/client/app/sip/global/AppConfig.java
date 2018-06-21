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
    public static String server_ip = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "serverip", "");
    //登录端口
    public  static  int server_port = 2010;
    //发送心跳的端口
    public static  int heart_port = 2020;
    //数据格式
    public static String dataFormat = "GB2312";
    //video 请求的数据头
    public static String video_header_id = "ZKTH";

    //sip信息 服务器地址
    public static String native_sip_server_ip = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "sip_server_ip", "");

    //sip号码
    public static String native_sip_name = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "sip_name", "");

    //sip密码
    public static String native_sip_pass = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "sip_pass", "");


    //sip服务器管理员密码
    public static String sipServerPass = "123456";
    //sip服务器获取所有的sip用户信息
    public static String sipServerDataUrl = "http://"+native_sip_server_ip+":8080/openapi/localuser/list?{\"syskey\":\""+sipServerPass+"\"}";

}
