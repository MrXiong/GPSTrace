package com.jgw.gpstrace;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.jgw.gpstrace.base.ToolBarActivity;
import com.jgw.gpstrace.bean.Code;
import com.jgw.gpstrace.bean.Message;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

import static com.jgw.gpstrace.bean.Code.CODE;

public class ScanResultActivity extends ToolBarActivity {

    @BindView(R.id.rv_list)
    NotRollRecyclerView mRvList;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_node_name)
    TextView tvNodeName;
    @BindView(R.id.ll_node_father)
    LinearLayout llNodeFather;
    @BindView(R.id.tv_empty_message)
    TextView tvEmptyMessage;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    @BindView(R.id.map_list)
    MapView mMapView;
    @BindView(R.id.sc_list)
    ScrollView scList;

    private List<Code> mCodeList;
    private CommonAdapter mCommonAdapter;

    //地图
    private BaiduMap mBaiduMap;
    private BDLocation mLocation;
    private float radius;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        String code = getIntent().getStringExtra(CODE);
        requestLocalData(code);

    }

    //请求本地的溯源信息
    private void requestLocalData(final String code) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                mCodeList = DataSupport.where("code = ?", code) .order("id desc").find(Code.class, true);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ListUtils.isEmpty(mCodeList)) {
                            llNull.setVisibility(View.VISIBLE);
                            return;
                        }
                        showList();
                        initMap();
                    }
                });
            }
        }).start();
    }


    private void showList() {
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(ScanResultActivity.this);
        mRvList.setNestedScrollingEnabled(false);
        mRvList.setLayoutManager(linearLayoutManager);

        //子类集合---------------------------------------------------------------------
        mCommonAdapter = new CommonAdapter<Code>(ScanResultActivity.this, R.layout.listitem_node, mCodeList) {
            @Override
            public void convert(ViewHolder holder, final Code code, int position) {


                holder.setText(R.id.tv_position, (position + 1) + "");
                holder.setText(R.id.tv_node_name, code.getNodeName() + code.getLatitude() + "==" + code.getLongitude());


                //子类集合---------------------------------------------------------------------
                List<Message> MessageList = mCodeList.get(position).getMessageList();
                if (ListUtils.isEmpty(MessageList)) {
                    MessageList = new ArrayList<Message>();
                }
                RecyclerViewFinal rvList = holder.getView(R.id.rv_list);

                FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(ScanResultActivity.this);
                rvList.setNestedScrollingEnabled(false);
                rvList.setLayoutManager(linearLayoutManager);

                rvList.setAdapter(new CommonAdapter<Message>(ScanResultActivity.this, R.layout.listitem_node_details, MessageList) {
                    @Override
                    protected void convert(ViewHolder holder, Message message, int position) {
                        holder.setText(R.id.tv_field_name, message.getKey());
                        holder.setText(R.id.tv_field_value, message.getValue());
                    }
                });

            }
        };
        mRvList.setAdapter(mCommonAdapter);
    }

    //----------------------------------------地图渲染----------------------------------------------


    private void initMap() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        initLocation();


        View childAt = mMapView.getChildAt(0);
        childAt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scList.requestDisallowInterceptTouchEvent(false);
                } else {
                    scList.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

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


        List<LatLng> latLngs = configData(ll);

        if (latLngs.size() <= 1) {
            latLngs.add(latLngs.get(0));
            paintLine(latLngs);
            TextView textView = new TextView(this);
            textView.setTextColor(Color.RED);
            textView.setText(mCodeList.get(0).getNodeName());
            drawText(latLngs.get(0), textView);
        } else {
            paintLine(latLngs);
            paintText(latLngs);
        }

    }

    private void paintLine(List<LatLng> latLngs) {
        OverlayOptions ooPolyline = new PolylineOptions().width(8).color(0xAAFF0000).points(latLngs);

        //在地图上画出线条图层，mPolyline：线条图层
        Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        mPolyline.setZIndex(3);


    }

    private void paintText(List<LatLng> latLngs) {
        //在对应的地点画字
        for (int i = 0; i < latLngs.size(); i++) {
            TextView textView = new TextView(this);
            textView.setTextColor(Color.RED);
            textView.setText(mCodeList.get(i).getNodeName());
            drawText(latLngs.get(i), textView);
        }
    }


    private List<LatLng> configData(LatLng ll) {
        List<LatLng> latLngList = new ArrayList<>();
        for (int i = 0; i < mCodeList.size(); i++) {
            Code code = mCodeList.get(i);
            LatLng latLng = new LatLng(code.getLatitude(), code.getLongitude());
            latLngList.add(latLng);
        }
        return latLngList;
    }

    private void drawText(LatLng latLng, TextView textView) {

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


    //String[] citys = {"起点","航线2","航线3","航线4","航线5","航线6","终点"};
}
