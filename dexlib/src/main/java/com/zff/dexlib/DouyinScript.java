package com.zff.dexlib;

import android.accessibilityservice.AccessibilityService;

import com.zff.dexlib.core.AppInfo;
import com.zff.dexlib.core.BaseScript;

/**
 * 抖音脚本
 */
public class DouyinScript extends BaseScript {

//    public DouyinScript(AppInfo appInfo) {
//        super(appInfo);
//    }

    public DouyinScript(AccessibilityService accessibilityService) {
        super(accessibilityService);
    }
    @Override
    public int getMaxSleepTime() {
        return 0;
    }

    @Override
    public int getMinSleepTime() {
        return 0;
    }

    @Override
    public void executeScript() {
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scrollup();
        }
    }


    @Override
    public boolean isDestinationPage() {
        return false;
    }
}
