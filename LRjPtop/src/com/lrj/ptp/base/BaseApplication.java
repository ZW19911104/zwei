package com.lrj.ptp.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import cn.jpush.android.api.JPushInterface;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lrj.ptp.utils.LOGUtils;

/**
 * 包名：com.lrj.ptp.base
 * 描述：
 * User 张伟
 * QQ:326093926
 * Date 2015/12/4 0004.
 * Time 上午 10:10.
 * 修改日期：
 * 修改内容：
 */
public class BaseApplication extends Application {
    private static final String BANGBANG_SETTING = "lrjptop";
    private static BaseApplication application;

    private SharedPreferences settings;

    private MyLocationListener mMyLocationListener;
    public LocationClient mLocationClient;
    public String longitude;//经度
    public String latitude;//纬度

    public String pathState;//记录当前登录的用户名 使用这个用户名创建文件夹
    public String custPathState;//记录当前用户的客户名称
    public String creditPathState = "0";//记录贷款类型
    public String shootPathState;//拍照类型

    public static BaseApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // 初始化百度定位器
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
//        mLocationClient.start();

        //初始化极光推送
//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush

        //初始化SharedPreferences
        settings = this.getSharedPreferences(BANGBANG_SETTING, Context.MODE_PRIVATE);

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationClient.setLocOption(option);
    }


    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            setSharedPreferencesValue("latitude", location.getLatitude() + "");
            setSharedPreferencesValue("lontitude", location.getLongitude() + "");
            setSharedPreferencesValue("address", location.getAddrStr());
            setSharedPreferencesValue("city", location.getCity());
            LOGUtils.getLogger().i(sb.toString());
            latitude = getSharedPreferencesValue("latitude");
            longitude = getSharedPreferencesValue("lontitude");
        }

    }


    public String getSharedPreferencesValue(String key) {
        if (settings == null) {
            settings = this.getSharedPreferences(BANGBANG_SETTING, Context.MODE_PRIVATE);
        }
        return settings.getString(key, "");
    }

    public void setSharedPreferencesValue(String key, String value) {
        if (settings == null) {
            settings = this.getSharedPreferences(BANGBANG_SETTING, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public void setSharedPreferencesBooleanValue(String key, boolean value) {
        if (settings == null) {
            settings = this.getSharedPreferences(BANGBANG_SETTING, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getSharedPreferencesBooleanValue(String key, boolean value) {
        if (settings == null) {
            settings = this.getSharedPreferences(BANGBANG_SETTING, Context.MODE_PRIVATE);
        }
        return settings.getBoolean(key, value);

    }

    public void clearSharepreference() {
        if (settings == null) {
            settings = this.getSharedPreferences(BANGBANG_SETTING, Context.MODE_PRIVATE);
        }
        settings.edit().clear().commit();
    }
}
