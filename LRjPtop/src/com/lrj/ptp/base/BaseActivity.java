package com.lrj.ptp.base;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.ab.util.AbWifiUtil;

import java.util.List;

/**
 * 包名：com.lrj.ptp.base
 * 描述：所有activity类的基类
 * User 张伟
 * QQ:326093926
 * Date 2015/12/4 0004.
 * Time 上午 10:48.
 * 修改日期：
 * 修改内容：
 */
public class BaseActivity extends FragmentActivity {
    protected BaseDialog baseDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseDialog = new BaseDialog(this,"加载中...");
        baseDialog.setCancelable(true);
    }

    /*
	 * 网络判断
	 */
    protected boolean getNetworkState() {
        boolean b = AbWifiUtil.isConnectivity(this);
        if (!b) {
            showCustomToast("无网络请检查网络连接");
        }
        return b;
    }

    protected void showCustomToast(String title){
        Toast.makeText(this,title,Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载框 在网络请求时 弹出的
     */
    protected void showLoadingDialog() {
        //这里换成这种方式避免网络慢的时候等待对话框显示太慢
        if (baseDialog != null && !baseDialog.isShowing()) {
            baseDialog.show();
        }
    }

    protected void dismissLoadingDialog() {
        if (baseDialog != null && baseDialog.isShowing()) {
            baseDialog.dismiss();
        }
    }

    /**
     *
     * @param context
     * @return true:在后台 false:前台
     */
    public static boolean isBackground(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName())) {
                    if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                        Log.i("后台", appProcess.processName);
                        return true;
                    } else {
                        Log.i("前台", appProcess.processName);
                        return false;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }


}
