package com.lrj.ptp.photograph;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.lrj.ptp.photograph.utils.BitmapDisplayer;

/**
 * 包名：com.lrj.ptp.photograph
 * 描述：
 * User 张伟
 * QQ:326093926
 * Date 2015/12/3 0003.
 * Time 上午 10:03.
 * 修改日期：
 * 修改内容：
 */
public class MatrixBitmapDisplayer implements BitmapDisplayer {

    public MatrixBitmapDisplayer() {

    }

    @Override
    public void display(Bitmap bitmap, ImageView imageView) {
        //正常的图片设置为FIT_CENTER
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void display(int resouceID, ImageView imageView) {
        // 加载前和出错的的图片不自动拉伸
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(resouceID);
    }
}
