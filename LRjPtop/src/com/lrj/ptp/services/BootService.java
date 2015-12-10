package com.lrj.ptp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.lrj.ptp.utils.LOGUtils;

/**
 * 包名：com.lrj.ptp.services
 * 描述：开机启动service
 * User 张伟
 * QQ:326093926
 * Date 2015/12/4 0004.
 * Time 下午 4:11.
 * 修改日期：
 * 修改内容：
 */
public class BootService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LOGUtils.getLogger().i("BootService退出了");
    }
}
