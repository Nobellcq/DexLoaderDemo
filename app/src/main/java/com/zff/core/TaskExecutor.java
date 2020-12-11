package com.zff.core;



import com.zff.MyApplication;
import com.zff.dynamicloading.FileUtils;
import com.zff.dynamicloading.PackageUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * 任务执行器
 */
public class TaskExecutor {

    private TaskInfo taskInfo;

    private boolean isStarted = false;
    private boolean pause = false;
    private boolean forcePause = false;
    private boolean isFinished = true;
    private AppInfo currentTestApp;
    private IScript currentScript;

    private Thread scriptThread;
    private Thread monitorThread;

    private static class TaskExecutorHolder {
        private static TaskExecutor instance = new TaskExecutor();
    }

    public TaskExecutor() {
    }

    public static TaskExecutor getInstance() {
        return TaskExecutorHolder.instance;
    }

    public void startTask(final TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
        this.initStartFlags();
        if(scriptThread == null) {
            scriptThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<AppInfo> appInfos = taskInfo.getAppInfos();
                        for (AppInfo info : appInfos) {
                            currentTestApp = info;
                            IScript script = null;
                            switch (info.getPkgName()) {
                                case Constants.pkg_douyin:
                                    File cacheFile = MyApplication.getAppInstance().getDir("dex",  0x0000);
                                    String internalPath = cacheFile.getAbsolutePath() + File.separator + "myjar_dex.jar";
                                    File dexFile = new File(internalPath);

                                    try {
                                        if (dexFile.exists()) {
                                            dexFile.delete();
                                        }
                                        dexFile.createNewFile();
                                        FileUtils.copyFiles(MyApplication.getAppInstance(), "myjar_dex.jar", dexFile);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    DexClassLoader dexClassLoader = new DexClassLoader(internalPath, cacheFile.getAbsolutePath(), null, MyApplication.getAppInstance().getClassLoader());
                                    Class<?> clz = null;
                                    try {

                                        clz = dexClassLoader.loadClass("com.zff.dexlib.DouyinScript");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                            }
                                    script = (IScript) clz.newInstance();
//                                    script = new DouyinScript(info);
                                    break;

                            }
                            if (script != null) {
                                currentScript = script;
                                script.execute();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Logger.d("执行完成");
                        // 执行完成
                        resetFlags();
                        PackageUtils.startSelf();
                    }
                }
            });
            scriptThread.start();


        } else {
            if(currentScript != null) {
                Logger.d("不为无");
                currentScript.resetStartTime();
//                currentScript.startApp();
            } else {
                Logger.e("不可能走这里，如果走这里，程序出bug了");
            }
        }
    }



    /**
     * 初始化标记
     */
    private void initStartFlags() {
        this.isStarted = true;
        this.pause = false;
        this.isFinished = false;
        this.forcePause = false;
    }

    /**
     * 重置所有标记
     */
    private void resetFlags() {
        isFinished = true;
        isStarted = false;
        setPause(true);
        setForcePause(true);
    }

    /**
     * 停止任务
     */
    public void stop(boolean force) {
        setForcePause(force);
        setPause(true);
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isPause() {
        return pause;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isForcePause() {
        return forcePause;
    }

    public void setForcePause(boolean forcePause) {
        this.forcePause = forcePause;
    }

    public AppInfo getCurrentTestApp() {
        return currentTestApp;
    }
}
