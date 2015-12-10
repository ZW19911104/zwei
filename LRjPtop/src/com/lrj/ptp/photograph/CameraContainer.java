package com.lrj.ptp.photograph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.widget.Toast;
import com.lrj.ptp.base.BaseApplication;
import com.lrj.ptp.db.DbHelper;
import com.lrj.ptp.db.domain.FileInfo;
import com.lrj.ptp.photograph.utils.FileOperateUtil;
import com.lrj.ptp.utils.LOGUtils;

import java.io.*;

/**
 * 包名：com.lrj.ptp.photograph
 * 描述：处理拍照和录屏后的图片 拍照和录屏返回的byte数据处理类
 * User 张伟
 * QQ:326093926
 * Date 2015/12/3 0003.
 * Time 上午 11:36.
 * 修改日期：
 * 修改内容：
 */
public class CameraContainer {
    private Context context;
    private String mThumbnailFolder;
    public CameraContainer(Context context) {
        this.context = context;
    }

    /**
     * 保存图片
     */
    public Bitmap save(byte[] data,String save) {
        if (data != null) {
            mThumbnailFolder = FileOperateUtil.getFolderPath(context,save);
            File folder = new File(mThumbnailFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            //解析生成相机返回的图片
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

            //产生新的文件名
            String imgName = FileOperateUtil.createFileNmae(".jpg");
            String imagePath = mThumbnailFolder + File.separator + imgName;

            File file = new File(imagePath);
            try {
                //存图片大图
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.flush();
                fos.close();
                //插入到数据库 把当前照片的名字和属于哪个类型
                FileInfo fileInfo = new FileInfo();
                fileInfo.filename  = imgName;
                fileInfo.fileleng = (int) file.length();
                fileInfo.filePath = file.getPath();
                fileInfo.custname = BaseApplication.getInstance().custPathState;
                fileInfo.username = BaseApplication.getInstance().pathState;
                DbHelper.getInstance(context).save(fileInfo);
                return bm;
            } catch (Exception e) {
                Toast.makeText(context, "解析相机返回流失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "拍照失败，请重试", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


}
