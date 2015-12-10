package com.lrj.ptp.photograph.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 包名：com.lrj.ptp.photograph
 * 描述：
 * User 张伟
 * QQ:326093926
 * Date 2015/12/2 0002.
 * Time 下午 7:20.
 * 修改日期：
 * 修改内容：
 */
public interface BitmapDisplayer {
    void display(Bitmap bitmap, ImageView imageView);
    void display(int resouceID,ImageView imageView);
}
