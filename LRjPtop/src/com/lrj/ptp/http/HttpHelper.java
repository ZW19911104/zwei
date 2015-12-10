package com.lrj.ptp.http;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;

/**
 * 包名：com.lrj.ptp.http
 * 描述：http网络请求帮助类
 * User 张伟
 * QQ:326093926
 * Date 2015/12/4 0004.
 * Time 上午 9:52.
 * 修改日期：
 * 修改内容：
 */
public class HttpHelper {
    private static HttpHelper  helper = new HttpHelper();
    private static HttpUtils utils;
    private HttpHelper(){
        utils = new HttpUtils();
    }

    public static HttpHelper getInstance(){
        return helper;
    }

    /**
     * 下载文件
     * @param method 请求方式
     * @param url 网络地址
     * @param tag 保存的文件地址+文件名称 比如：mnt/sdcard/1234.jpg
     * @param params 参数
     * @param requestCallBack
     */
    public void download(HttpRequest.HttpMethod method,String url,String tag,RequestParams params,RequestCallBack<File> requestCallBack){
        utils.download(method, url, tag,params ,true, false, requestCallBack);
    }

    /**
     * 发送文本 图片 文件 请求 到服务器端
     * @param method
     * @param url
     * @param params
     * @param requestCallBack
     */
    public void sendTo(HttpRequest.HttpMethod method,String url,RequestParams params,RequestCallBack<String> requestCallBack){
        if(params==null) {
            utils.send(method, url, requestCallBack);
        }else{
            utils.send(method, url, params, requestCallBack);
        }
    }
}
