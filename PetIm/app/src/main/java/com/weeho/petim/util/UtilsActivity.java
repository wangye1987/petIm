package com.weeho.petim.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import com.weeho.petim.modle.MyAppInfo;

/**
 * Created by wangkui on 2017/5/16.
 */

public class UtilsActivity {
    public static List<MyAppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
//            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
//                continue;
//            }
                if("com.example.exam_hospital".equals(packageInfo.packageName)) {
                    MyAppInfo myAppInfo = new MyAppInfo();
                    myAppInfo.setAppName(packageInfo.packageName);
                    if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                        continue;
                    }
                    myAppInfo.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                    myAppInfos.add(myAppInfo);
                }
            }
        }catch (Exception e){
//            Log.e(TAG,"===============获取应用包信息失败");
        }
        return myAppInfos;
    }
}
