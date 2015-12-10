package com.lrj.ptp.utils;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 包名：com.lrj.ptp.utils
 * 描述：json工具类
 * User 张伟
 * QQ:326093926
 * Date 2015/12/4 0004.
 * Time 上午 10:55.
 * 修改日期：
 * 修改内容：
 */
public class JSONUtils {

    /**
     * 定位后 获取的json
     * @param latitude   纬度
     * @param longitude  经度
     * @param address    地址
     * @param city       城市
     * @return
     */
    public static String getStrTjson(String latitude,String longitude,String address,String city){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat",latitude);
            jsonObject.put("lon",longitude);
            jsonObject.put("address",address);
            jsonObject.put("city",city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 对象转json
     * @param tClass 需要转成json的对象
     * @return
     */
    public static String getObjTjson(Class<?> tClass){
        Gson gson = new Gson();
        return gson.toJson(tClass);
    }


}
