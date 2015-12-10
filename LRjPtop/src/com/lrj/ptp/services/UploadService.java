package com.lrj.ptp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 包名：com.lrj.ptp.services
 * 描述：图片和资料上传服务类
 * User 张伟
 * QQ:326093926
 * Date 2015/12/10 0010.
 * Time 上午 10:11.
 * 修改日期：
 * 修改内容：
 */
public class UploadService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
}
