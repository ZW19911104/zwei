package com.lrj.ptp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.lrj.ptp.services.BootService;
import com.lrj.ptp.utils.LOGUtils;

/**
 * 包名：com.lrj.ptp.receivers
 * 描述：开机启动广播监测
 * User 张伟
 * QQ:326093926
 * Date 2015/12/4 0004.
 * Time 下午 4:33.
 * 修改日期：
 * 修改内容：
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Intent newIntent = new Intent(context,BootService.class);
            context.startService(newIntent);
            LOGUtils.getLogger().i("LRjPtop我启动了");
        }
    }
}
