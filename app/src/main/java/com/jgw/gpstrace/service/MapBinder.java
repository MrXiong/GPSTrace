package com.jgw.gpstrace.service;

import android.os.Binder;

import com.baidu.location.BDLocation;

/**
 * Created by user on 2018/4/3.
 */

public class MapBinder extends Binder {
    private BDLocation location;

    public BDLocation getLocation() {
        return location;
    }

    public void setLocation(BDLocation location) {
        this.location = location;
    }
}
