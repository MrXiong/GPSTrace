package com.jgw.gpstrace.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by user on 2018/4/3.
 */

public class MyLocation extends DataSupport implements Serializable {
    //位置
    private double latitude;
    private double longitude;
    private long time;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
