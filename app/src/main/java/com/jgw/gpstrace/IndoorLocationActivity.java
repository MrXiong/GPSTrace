package com.jgw.gpstrace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.jgw.gpstrace.bean.MyLocation;
import com.jgw.gpstrace.bean.Node;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.loadingviewfinal.HeaderAndFooterRecyclerViewAdapter;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

import static com.jgw.gpstrace.Contacts.PARENT_NODE;
import static com.jgw.gpstrace.bean.Code.CODE;

public class IndoorLocationActivity extends BaseScanActivity {

    @BindView(R.id.ll_my)
    LinearLayout llMy;
    @BindView(R.id.ll_sync)
    LinearLayout llSync;
    @BindView(R.id.tv_scan_select)
    TextView tvScanSelect;
    @BindView(R.id.rv_list)
    RecyclerViewFinal mRvList;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    private List<Node> mNodeList = new ArrayList<>();
    private CommonAdapter mCommonAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_location);
        ButterKnife.bind(this);
        setEmptyToolbar();
        setScanBackCallBack(new ScanBackCallBack() {
            @Override
            public void scanBackHandleDo(String scanResult) {
                //Toast.makeText(getApplicationContext(), "码" + scanResult, Toast.LENGTH_SHORT).show();
                Intent in = new Intent(IndoorLocationActivity.this, ScanResultActivity.class);
                in.putExtra(Contacts.LOCATION, mLocation);
                in.putExtra(CODE, scanResult);
                startActivity(in);
            }
        });


        configData();


        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mCommonAdapter = new CommonAdapter<Node>(this, R.layout.listitem_node_config, mNodeList) {
            @Override
            protected void convert(ViewHolder holder, Node node, int position) {
                holder.setText(R.id.tv_node_name, node.getNodeName());
            }
        };
        mRvList.setAdapter(mCommonAdapter);
        mRvList.setOnItemClickListener(new HeaderAndFooterRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                Intent in = new Intent(IndoorLocationActivity.this, RecordActivity.class);
                Node node = mNodeList.get(position);
                if(mLocation == null) {
                    Toast.makeText(getApplicationContext(), "正在定位，请稍等", Toast.LENGTH_SHORT).show();
                    return;
                }
                node.setLatitude(mLocation.getLatitude());
                node.setLongitude(mLocation.getLongitude());

                in.putExtra(Node.NODE, node);
                startActivity(in);
            }
        });
    }


/*    private void startLocation() {

        Intent in = new Intent(this, MapService.class);
        bindService(in, serviceConnection, Service.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MapBinder mapBinder = ((MapBinder) service);
            Log.v("tag", "链接建立啦。。。。" + mapBinder.getLocation().getLatitude());

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };*/


    //构建数据
    private void configData() {

        List<String[]> childs = new ArrayList<>();
        childs.add(Contacts.DENG_JIS);
        childs.add(Contacts.LI_GANGS);
        childs.add(Contacts.BU_LAOS);
        childs.add(Contacts.CHECKS);
        childs.add(Contacts.RU_GANGS);

        for (int i = 0; i < PARENT_NODE.length; i++) {
            Node node = new Node();
            node.setNodeName(PARENT_NODE[i]);
            node.setNodes(childs.get(i));
            mNodeList.add(node);
        }


    }


    @OnClick({R.id.ll_my, R.id.ll_sync})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_my:
                if(!locationService.isStart()) {
                    ivLocation.setVisibility(View.GONE);
                    tvLocation.setVisibility(View.VISIBLE);
                    tvLocation.setText("正在定位");
                    initLocation();
                }


                break;
            case R.id.ll_sync:
                if (mLocation == null) {
                    Toast.makeText(IndoorLocationActivity.this, "定位失败，请在室外定位", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent in = new Intent(IndoorLocationActivity.this, TraceActivity.class);
                in.putExtra(Contacts.LOCATION, mLocation);
                startActivity(in);

                break;
        }
    }


    //-----------------------------------------定位-----------------------------------------------
    private LocationService locationService;
    private BDLocation mLocation;

    private void initLocation() {
        locationService = ((IApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }

        locationService.start();// 定位SDK
    }

    @Override
    protected void onStart() {
        super.onStart();
        initLocation();
    }


    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mLocation = location;
                if(location == null) {
                    return;
                }

                ivLocation.setVisibility(View.GONE);
                tvLocation.setVisibility(View.VISIBLE);

                //String addrStr = location.getAddrStr();
                String addrStr = location.getDistrict();

                if(TextUtils.isEmpty(addrStr)) {
                    tvLocation.setText(location.getLatitude() +"\n"+ location.getLongitude());
                }else {
                    tvLocation.setText(addrStr);
                }

                // Toast.makeText(IndoorLocationActivity.this, mLocation.getLatitude() + "=="+mLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                saveLocation(location);

                //测试
/*                MyLocation myLocation = new MyLocation();
                myLocation.setLatitude(30.285338);
                myLocation.setLongitude(120.126185);
                myLocation.setAddress("地址");

                MyLocation myLocation1 = new MyLocation();
                myLocation1.setLatitude(30.287147);
                myLocation1.setLongitude(120.127142);
                myLocation1.setAddress("地址1");

                MyLocation myLocation2 = new MyLocation();
                myLocation2.setLatitude(30.286944);
                myLocation2.setLongitude(120.129127);
                myLocation2.setAddress("地址2");

                myLocation.save();
                myLocation1.save();
                myLocation2.save();*/
            }
        }

    };


    private void saveLocation(BDLocation location) {
        MyLocation myLocation = new MyLocation();
        myLocation.setLatitude(location.getLatitude());
        myLocation.setLongitude(location.getLongitude());

        String address = location.getAddrStr();
        String addressnew = address.substring(address.length() - 5, address.length());
        myLocation.setAddress(addressnew);
        myLocation.save();
    }

    @Override
    protected void onStop() {
        if (null == locationService) {
            return;
        }
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }
}
