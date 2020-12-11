package com.zff;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.squareup.otto.Subscribe;
import com.zff.core.Logger;
import com.zff.core.MyAccessbilityService;
import com.zff.core.bus.BusEvent;
import com.zff.core.bus.BusManager;
import com.zff.core.TaskExecutor;
import com.zff.core.AppInfo;
import com.zff.core.TaskInfo;

import java.util.List;

import static com.zff.core.bus.EventType.no_roots_alert;
import static com.zff.core.bus.EventType.set_accessiblity;


public class MyApplication extends Application {

    private static final String TAG = "MainActivity";

    private MyAccessbilityService accessbilityService;
    protected static MyApplication appInstance;


    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }



    //MyApplication 没有申明啊！！
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.setDebug(true);
        appInstance = this;
        context = getApplicationContext();
        BusManager.getBus().register(this);

    }

    @Subscribe
    public void subscribeEvent(BusEvent event) {
        switch (event.getType()) {

            case set_accessiblity:
                Log.d(TAG, "subscribeEvent: 成功收到");
                Toast.makeText(getApplicationContext(), "服务启动成功！", Toast.LENGTH_LONG).show();
                this.accessbilityService = (MyAccessbilityService) event.getData();
                break;
            case no_roots_alert:
                Log.d(TAG, "subscribeEvent: sos");
                Toast.makeText(getApplicationContext(), "sos！", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static MyApplication getAppInstance() {
        return appInstance;
    }

    public MyAccessbilityService getAccessbilityService() {
        return accessbilityService;
    }


    public void hh() {
        accessbilityService.speak();
    }

    public boolean isAccessbilityServiceReady() {
        return accessbilityService != null;
    }

    /**
     * 开始执行任务
     */
    public void startTask(List<AppInfo> appInfos) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setAppInfos(appInfos);
        TaskExecutor.getInstance().startTask(taskInfo);
    }

}
