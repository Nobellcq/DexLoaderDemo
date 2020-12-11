package com.zff.dexlib.core;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Build;


import java.util.Random;

//import com.zff.dynamicloading.PackageUtils;

public abstract class BaseScript implements IScript {

    private AppInfo appInfo;
    private long startTime;

    private AccessibilityService accessibilityService;
    public BaseScript(AccessibilityService accessibilityService) {
        this.accessibilityService = accessibilityService;
    }



    protected long getTimeout() {
        return appInfo.getPeriod() * 60 * 60 * 1000;
    }

    @Override
    public void execute() {
//        startApp();
        resetStartTime();
        executeScript();
    }



    @Override
    public AppInfo getAppInfo() {
        return appInfo;
    }

    @Override
    public void startApp() {
//        PackageUtils.startApp(getAppInfo().getPkgName());
    }

    @Override
    public void resetStartTime() {
        this.startTime = System.currentTimeMillis();
    }



    public void scrollup(){
        swipe(540,1000,540,100,10);
    }


    public boolean swipe(int fromX, int fromY, int toX, int toY, int steps) {
        if (Build.VERSION.SDK_INT >= 24) {
            GestureDescription.Builder builder = new GestureDescription.Builder();
            Path path = new Path();
            path.moveTo(fromX, fromY);
            path.lineTo(toX, toY);
            GestureDescription gestureDescription = builder
                    .addStroke(new GestureDescription.StrokeDescription(path, 100, 220))
                    .build();
            if (accessibilityService.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            }, null)) {
                //  isAllowPlay = false;
            }
        }
        return true;
    }


    /**


    /**
     * 获取最大休眠时间
     *
     * @return
     */
    protected abstract int getMaxSleepTime();

    /**
     * 获取最小休眠时间
     *
     * @return
     */
    protected abstract int getMinSleepTime();

    /**
     * 执行脚本
     */
    public abstract void executeScript();
}
