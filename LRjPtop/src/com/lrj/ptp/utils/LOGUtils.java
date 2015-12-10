package com.lrj.ptp.utils;

import android.util.Log;

/**
 * 包名：com.lrj.ptp.utils
 * 描述：重构Log日志输出类 调用方法，如：LogUtil.getLogger.x();
 * User 张伟
 * QQ:326093926
 * Date 2015/12/2 0002.
 * Time 下午 7:08.
 * 修改日期：
 * 修改内容：
 */
public class LOGUtils {
    public final static boolean DEBUG = true;//控制开关
    private final static String tag = "lrjptopLog";

    public static int logLevel = Log.VERBOSE;
    private static LOGUtils logger = new LOGUtils();;
    public static LOGUtils getLogger() {
        return logger;
    }

    private LOGUtils() {
        //do sth
    }

    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            return "[ " + Thread.currentThread().getName() + ": "
                    + st.getFileName() + ":" + st.getLineNumber() + " ]>>>>>>>>";
        }
        return null;
    }



    public void i(Object str) {
        if (!LOGUtils.DEBUG)
            return;
        if (logLevel <= Log.INFO) {
            String name = getFunctionName();
            if (name != null) {
                Log.i(tag, name + " - " + str);
            } else {
                Log.i(tag, str.toString());
            }
        }
    }

    public void v(Object str) {
        if (!LOGUtils.DEBUG)
            return;
        if (logLevel <= Log.VERBOSE) {
            String name = getFunctionName();
            if (name != null) {
                LOGUtils.getLogger().i(name + " - " + str);
            } else {
                LOGUtils.getLogger().i(str.toString());
            }
        }
    }

    public void w(Object str) {
        if (!LOGUtils.DEBUG)
            return;
        if (logLevel <= Log.WARN) {
            String name = getFunctionName();
            if (name != null) {
                Log.w(tag, name + " - " + str);
            } else {
                Log.w(tag, str.toString());
            }
        }
    }

    public void e(Object str) {
        if (!LOGUtils.DEBUG)
            return;
        if (logLevel <= Log.ERROR) {
            String name = getFunctionName();
            if (name != null) {
                Log.e(tag, name + " - " + str);
            } else {
                Log.e(tag, str.toString());
            }
        }
    }

    public void e(Exception ex) {
        if (!LOGUtils.DEBUG)
            return;
        if (logLevel <= Log.ERROR) {
            Log.e(tag, "error", ex);
        }
    }

    public void d(Object str) {
        if (!LOGUtils.DEBUG)
            return;
        if (logLevel <= Log.DEBUG) {
            String name = getFunctionName();
            if (name != null) {
                Log.d(tag, name + " - " + str);
            } else {
                Log.d(tag, str.toString());
            }
        }
    }

}
