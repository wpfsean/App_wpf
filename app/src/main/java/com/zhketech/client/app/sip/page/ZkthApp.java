package com.zhketech.client.app.sip.page;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Root on 2018/6/19.
 */

public class ZkthApp extends Application {
    private static ZkthApp mApplition;
    private  List<Activity> activityList = new LinkedList<>();
    public  static   int direction = 2;//1、竖屏 2、横屏
    public static String serverIp = "19.0.0.11";
    @Override
    public void onCreate() {
        super.onCreate();
        mApplition = this;
    }

    public static ZkthApp getInstance() {
        return mApplition;
    }

    /**
     * 移除Activity
     */
    public  void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 添加activity
     */
    public  void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 移除所有的activity
     */
    public  void exit() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
//        System.exit(0);
    }
    //设置方向
    public void setDirection(int p){
        direction = p;
    }

    //设置ip
    public void setServerIp(String ip){
        serverIp = ip;
    }
}
