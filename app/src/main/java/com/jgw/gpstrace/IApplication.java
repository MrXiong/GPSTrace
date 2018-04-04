package com.jgw.gpstrace;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePalApplication;

/**
 * Created by user on 2018/4/2.
 */

public class IApplication extends Application {
    public LocationService locationService;
    public Vibrator mVibrator;

    //一小时定位一次
    public int SPAN_HOURS = 1000 * 60 * 60;
    //5秒定位一次
    public int SPAN_SECOND = 1000 * 60;

    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        locationService.setLocationOption(initLocationOption());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        LitePalApplication.initialize(this);

    }

    private LocationClientOption initLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setScanSpan(SPAN_SECOND);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        return option;
    }
}
