package com.zhketech.client.app.sip.global;

import com.zhketech.client.app.sip.page.ZkthApp;
import com.zhketech.client.app.sip.utils.SharedPreferencesUtils;

/**
 * Created by Root on 2018/6/19.
 */

public class AppConfig {
    public AppConfig() {
        throw new UnsupportedOperationException("not't constructed");
    }

    public static String current_user = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "username", "");
    public static String current_pass = (String) SharedPreferencesUtils.getObject(ZkthApp.getInstance(), "userpass", "");

}
