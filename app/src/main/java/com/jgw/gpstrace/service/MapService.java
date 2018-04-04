package com.jgw.gpstrace.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.jgw.gpstrace.IApplication;
import com.jgw.gpstrace.LocationService;

/**
 * Created by user on 2018/4/3.
 */

public class MapService extends Service {

    private LocationService locationService;
    private BDLocation mLocation;
    private MapBinder mapBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("tag", "-----------------我是Test   -- onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initLocation(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v("tag", "-----------------我是Test   -- onBind");
        return mapBinder;
    }

    private void initLocation(Intent intent) {
        locationService = ((IApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        //注册监听
        int type = intent.getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }

        locationService.start();// 定位SDK
    }


    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mLocation = location;
                mapBinder = new MapBinder();
                mapBinder.setLocation(location);



            }
        }

    };

    @Override
    public boolean onUnbind(Intent intent) {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        return super.onUnbind(intent);
    }
}
