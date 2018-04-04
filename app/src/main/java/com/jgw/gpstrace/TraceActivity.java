package com.jgw.gpstrace;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.jgw.gpstrace.base.ToolBarActivity;
import com.jgw.gpstrace.bean.MyLocation;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class TraceActivity extends ToolBarActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BDLocation mLocation;
    private float radius;
    private double latitude;
    private double longitude;

    //String[] citys = {"起点","航线2","航线3","航线4","航线5","航线6","终点"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);
        mMapView = findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        initLocation();

    }

    private void initLocation() {
        mLocation = getIntent().getParcelableExtra(Contacts.LOCATION);
        radius = mLocation.getRadius();
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();


        LatLng ll = new LatLng(latitude, longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


        //在地图上面画定位圆
        paintCicle();
        configData();
    }

    private void configData() {
        final List<LatLng> latLngs = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                final List<MyLocation> locations = DataSupport.findAll(MyLocation.class);
                if(ListUtils.isEmpty(locations)) {
                    return;
                }
                for (int i = 0; i < locations.size(); i++) {
                    MyLocation myLocation = locations.get(i);
                    LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    latLngs.add(latLng);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (latLngs.size() <= 1) {
                            latLngs.add(latLngs.get(0));
                            paintLine(latLngs);
                            TextView textView = new TextView(TraceActivity.this);
                            textView.setTextColor(Color.RED);
                            textView.setText(locations.get(0).getAddress());
                            drawText(latLngs.get(0), textView);
                        } else {
                            paintLine(latLngs);
                            paintText(latLngs, locations);
                        }

                    }
                });



            }
        }).start();

    }

    private void paintLine(List<LatLng> latLngs) {
        OverlayOptions ooPolyline = new PolylineOptions().width(8).color(0xAAFF0000).points(latLngs);

        //在地图上画出线条图层，mPolyline：线条图层
        Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        mPolyline.setZIndex(3);


    }

    private void paintText(List<LatLng> latLngs, List<MyLocation> locations) {
        //在对应的地点画字
        for (int i = 0; i < latLngs.size(); i++) {
            TextView textView = new TextView(this);
            textView.setTextColor(Color.RED);
            textView.setText(locations.get(i).getAddress());
            drawText(latLngs.get(i), textView);
        }
    }



    private void drawText(LatLng latLng, TextView textView){

        //将View转化为Bitmap
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(textView);
        OverlayOptions options = new MarkerOptions().position(latLng).icon(descriptor).zIndex(9).draggable(true);
        mBaiduMap.addOverlay(options);
    }



/*    private List<LatLng> configData(LatLng ll){

        Log.v("tag", ll.latitude+"============="+ll.longitude);

        List<LatLng> latLngList = new ArrayList<>();


        LatLng latLng = new LatLng(30.285381, 120.12754);
        LatLng latLng1 = new LatLng(30.285338,120.126185);
        LatLng latLng2 = new LatLng(30.287147, 120.127142);
        LatLng latLng3 = new LatLng(30.286944, 120.129127);
        LatLng latLng4 = new LatLng(30.28519, 120.128794);
        LatLng latLng5 = new LatLng(30.283923, 120.128961);
        LatLng latLng6 = new LatLng(30.283389, 120.127106);
        latLngList.add(latLng);
        latLngList.add(latLng1);
        latLngList.add(latLng2);
        latLngList.add(latLng3);
        latLngList.add(latLng4);
        latLngList.add(latLng5);
        latLngList.add(latLng6);


        return latLngList;

    }*/

    private void paintCicle() {
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(radius)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(latitude)
                .longitude(longitude).build();

        mBaiduMap.setMyLocationData(locData);
    }
}
