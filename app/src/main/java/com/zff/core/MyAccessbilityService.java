package com.zff.core;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;


import com.zff.Utils;
import com.zff.core.bus.BusEvent;
import com.zff.core.bus.BusManager;
import com.zff.core.bus.EventType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyAccessbilityService extends AccessibilityService {

    private int noRootCount = 0;
    private static final int maxNoRootCount = 3;
    private boolean isWork = false;

    private long cur = 0;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (System.currentTimeMillis() - cur > 3000) {
            cur = System.currentTimeMillis();
//            BusManager.getBus().post(new BusEvent<>(EventType.no_roots_alert));
            Log.d("wdnmd", "onAccessibilityEvent: 正执行");
        }

    }

    @Override
    public void onInterrupt() {

    }

    //不知道为何调用不了toast
    public void speak(){
        Utils.toast("hhhh");
        Log.d("wdnmd", "speak: ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("wdnmd", "onCreate: 成功转发");
//        Utils.toast("成功转发");
        BusManager.getBus().post(new BusEvent<>(EventType.set_accessiblity, this));
//        MyApplication.getAppInstance().setAccessbilityService();
//        BusManager.getBus().post(new BusEvent<>(EventType.set_accessiblity, this));
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Logger.d("MyAccessbilityService on start command");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        Logger.d("MyAccessbilityService on unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
//        Logger.d("MyAccessbilityService on rebind");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
//        Logger.d("MyAccessbilityService on task removed");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
//        Logger.d("MyAccessbilityService connected");
//        BusManager.getBus().post(new BusEvent<>(accessiblity_connected));
        isWork = true;
    }

    public boolean isWrokFine() {
        return isWork;
    }
}
