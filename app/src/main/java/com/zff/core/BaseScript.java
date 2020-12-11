package com.zff.core;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Build;

import com.zff.MyApplication;
//import com.zff.dynamicloading.PackageUtils;

import java.util.Random;

public abstract class BaseScript implements IScript {

    private AppInfo appInfo;
    private long startTime;

    public BaseScript(AppInfo appInfo) {
        this.appInfo = appInfo;
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

    /**
     * 获取一个随机的休眠时间
     *
     * @param min
     * @param max
     * @return
     */
    private int getRandomSleepTime(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
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
            if (MyApplication.getAppInstance().getAccessbilityService().dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
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
    protected abstract void executeScript();
}
