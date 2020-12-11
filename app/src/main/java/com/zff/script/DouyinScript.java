package com.zff.script;


import com.zff.core.BaseScript;
import com.zff.core.AppInfo;


/**
 * 抖音脚本
 */
public class DouyinScript extends BaseScript {

    public DouyinScript(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected int getMaxSleepTime() {
        return 0;
    }

    @Override
    protected int getMinSleepTime() {
        return 0;
    }

    @Override
    protected void executeScript() {
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
