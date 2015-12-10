package com.lrj.ptp.photograph.utils;

import android.content.Context;
import com.lrj.ptp.R;
import com.lrj.ptp.base.BaseApplication;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 包名：com.lrj.ptp.photograph.utils
 * 描述：文件操作工具类
 * User 张伟
 * QQ:326093926
 * Date 2015/12/3 0003.
 * Time 上午 10:09.
 * 修改日期：
 * 修改内容：
 */
public class FileOperateUtil {

    public final static int ROOT=0;//根目录
    public final static int TYPE_IMAGE=1;//图片
    public final static int TYPE_THUMBNAIL=2;//缩略图
    public final static int TYPE_VIDEO=3;//视频
    /**
     *获取文件夹路径
     * @return
     */
    public static String getFolderPath(Context context,String typepath) {
        //本业务文件主目录
        StringBuilder pathBuilder=new StringBuilder();
        //添加应用存储路径
        pathBuilder.append(context.getExternalFilesDir(null).getAbsolutePath());
        pathBuilder.append(File.separator);
        //添加用户文件夹
        pathBuilder.append(BaseApplication.getInstance().pathState);
        pathBuilder.append(File.separator);
        //添加用户文件夹下面的客户文件夹
        pathBuilder.append(BaseApplication.getInstance().custPathState);
        pathBuilder.append(File.separator);
        //贷款类型
        pathBuilder.append(BaseApplication.getInstance().creditPathState);
        pathBuilder.append(File.separator);
        if(typepath==null){
            return pathBuilder.toString();
        }else {
            //拍照类型
            pathBuilder.append(typepath);
            pathBuilder.append(File.separator);
            return pathBuilder.toString();
        }
    }

    /**
     * 获取目标文件夹内指定后缀名的文件数组,按照修改日期排序
     * @param file 目标文件夹路径
     * @param format 指定后缀名
     * @param content 包含的内容,用以查找视频缩略图
     * @return
     */
    public static List<File> listFiles(String file,final String format,String content){
        return listFiles(new File(file), format,content);
    }
    public static List<File> listFiles(String file,final String format){
        return listFiles(new File(file), format,null);
    }
    /**
     * 获取目标文件夹内指定后缀名的文件数组,按照修改日期排序
     * @param file 目标文件夹
     * @param extension 指定后缀名
     * @param content 包含的内容,用以查找视频缩略图
     * @return
     */
    public static List<File> listFiles(File file,final String extension,final String content){
        File[] files=null;
        if(file==null||!file.exists()||!file.isDirectory())
            return null;
        files=file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File arg0, String arg1) {
                // TODO Auto-generated method stub
                if(content==null||content.equals(""))
                    return arg1.endsWith(extension);
                else {
                    return  arg1.contains(content)&&arg1.endsWith(extension);
                }
            }
        });
        if(files!=null){
            List<File> list=new ArrayList<File>(Arrays.asList(files));
            sortList(list, false);
            return list;
        }
        return null;
    }

    /**
     *  根据修改时间为文件列表排序
     *  @param list 排序的文件列表
     *  @param asc  是否升序排序 true为升序 false为降序
     */
    public static void sortList(List<File> list,final boolean asc){
        //按修改日期排序
        Collections.sort(list, new Comparator<File>() {
            public int compare(File file, File newFile) {
                if (file.lastModified() > newFile.lastModified()) {
                    if (asc) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (file.lastModified() == newFile.lastModified()) {
                    return 0;
                } else {
                    if (asc) {
                        return -1;
                    } else {
                        return 1;
                    }
                }

            }
        });
    }

    /**
     *
     * @param extension 后缀名 如".jpg"
     * @return
     */
    public static String createFileNmae(String extension){
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
        // 转换为字符串
        String formatDate = format.format(new Date());
        //查看是否带"."
        if(!extension.startsWith("."))
            extension="."+extension;
        return formatDate+extension;
    }

    /**
     *  删除缩略图 同时删除源图或源视频
     *  @param thumbPath 缩略图路径
     *  @return
     */
    public static boolean deleteThumbFile(String thumbPath,Context context) {
        boolean flag = false;

        File file = new File(thumbPath);
        if (!file.exists()) { // 文件不存在直接返回
            return flag;
        }

        flag = file.delete();
        //源文件路径
        String sourcePath=thumbPath.replace(context.getString(R.string.Thumbnail),
                context.getString(R.string.Image));
        file = new File(sourcePath);
        if (!file.exists()) { // 文件不存在直接返回
            return flag;
        }
        flag = file.delete();
        return flag;
    }
    /**
     *  删除源图或源视频 同时删除缩略图
     *  @param sourcePath 缩略图路径
     *  @return
     */
    public static boolean deleteSourceFile(String sourcePath,Context context) {
        boolean flag = false;

        File file = new File(sourcePath);
        if (!file.exists()) { // 文件不存在直接返回
            return flag;
        }

        flag = file.delete();
        //缩略图文件路径
        String thumbPath=sourcePath.replace(context.getString(R.string.Image),
                context.getString(R.string.Thumbnail));
        file = new File(thumbPath);
        if (!file.exists()) { // 文件不存在直接返回
            return flag;
        }
        flag = file.delete();
        return flag;
    }


}
