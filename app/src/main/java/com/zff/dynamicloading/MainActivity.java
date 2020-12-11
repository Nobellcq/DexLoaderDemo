package com.zff.dynamicloading;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zff.core.MyAccessbilityService;
import com.zff.MyApplication;
import com.zff.core.AppInfo;
import com.zff.script.DouyinScript;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //单单只申明了还不够，虽然oncreate也会进行初始化，但没有context，记得开启
        startService(new Intent(getApplicationContext(), MyAccessbilityService.class));

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDexClass();
            }
        });
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<AppInfo> appInfos = new ArrayList<>();
                AppInfo appInfo = new AppInfo();
                appInfo.setAppName("ss");
                appInfo.setFree(true);
                appInfo.setIcon(1);
                appInfo.setName("ss");
                appInfo.setPeriod(20000);
                appInfo.setPkgName("com.ss.android.ugc.aweme");
                appInfo.setUuid("1251345");
                appInfos.add(appInfo);
                Log.d("wdnmd", "onClick: appinfos"+appInfos.size());
                Log.d("wdnmd", "onClick: appinfos"+appInfos.get(0).getPkgName());
                MyApplication.getAppInstance().hh();
                MyApplication.getAppInstance().startTask(appInfos);
            }
        });
    }

    private void loadDexClass() {
        File cacheFile = getDir("dex", MODE_PRIVATE);
        String internalPath = cacheFile.getAbsolutePath() + File.separator + "myjar_dex.jar";
        File dexFile = new File(internalPath);

        try {
            if (dexFile.exists()) {
                dexFile.delete();
            }
            dexFile.createNewFile();
//            if (!dexFile.exists()) {
//                dexFile.createNewFile();
//            }
            FileUtils.copyFiles(this, "myjar_dex.jar", dexFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //下面开始加载dex class
        //1.待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限,
        //2.解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写
        //3.指向包含本地库(so)的文件夹路径，可以设为null
        //4.父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()取到。
        final DexClassLoader dexClassLoader = new DexClassLoader(internalPath, cacheFile.getAbsolutePath(), null, getClassLoader());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    //该name就是internalPath路径下的dex文件里面的ShowToastImpl这个类的包名+类名
//                    Thread.currentThread().getContextClassLoader().
                    Class<?> clz = dexClassLoader.loadClass("com.zff.dexlib.DouyinScript");
                    //构造器构造出实例
                    Constructor<?> constructor = clz.getDeclaredConstructor(AccessibilityService.class);
                    Object scriptInstance = constructor.newInstance(MyApplication.getAppInstance().getAccessbilityService());
                    //这个只能说明是个实例，不一定要代入，要和后面的参数相呼应
                    Method con = clz.getDeclaredMethod("executeScript");
                    con.invoke(scriptInstance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();

    }


}
