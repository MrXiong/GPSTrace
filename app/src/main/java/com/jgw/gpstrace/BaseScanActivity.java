package com.jgw.gpstrace;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jgw.gpstrace.base.ToolBarActivity;
import com.jgw.gpstrace.scan.PreferencesUtils;
import com.jgw.gpstrace.scan.idata.IDataContants;
import com.jgw.gpstrace.scan.idata.IDataReceiver;
import com.jgw.gpstrace.scan.idata.IDataReceiverService;
import com.jgw.gpstrace.scan.seuic.SeuicBroadcastConfig;
import com.jgw.gpstrace.scan.seuic.SeuicReceiver;
import com.jgw.gpstrace.scan.seuic.SeuicReceiverService;
import com.jgw.gpstrace.scan.seuic.SeuicSettingHelp;
import com.jgw.gpstrace.scan.supoin.SupoinConstants;
import com.jgw.gpstrace.scan.supoin.SupoinReceiver;
import com.jgw.gpstrace.scan.supoin.SupoinReceiverService;


/**
 * Created by linpg on 2016/10/25.
 */
public class BaseScanActivity extends ToolBarActivity implements SupoinReceiverService,SeuicReceiverService,IDataReceiverService {
    public static final String IS_REPEAT_CODE = "is_repeat_code";
    protected SupoinReceiver supoinReceiver;
    protected SeuicReceiver seuicReceiver;
    protected boolean isCanScan = false;
    protected ScanBackCallBack scanBackCallBack;
    private boolean recoveryScan = true;//页面恢复是否启用扫描
    private IDataReceiver mIDataReceiver;

    public void setRecoveryScan(boolean recoveryScan) {
        this.recoveryScan = recoveryScan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScanParam();
    }

    /**
     * 初始化扫描参数
     */
    void initScanParam(){
        initIData();
        initSupoin();
        initSeuic();
        isCanScan = true;
    }
    //初始化IData扫码枪
     void initIData() {
         //扫描结果的意图过滤器的动作一定要使用"android.intent.action.SCANRESULT"
         IntentFilter intentFilter = new IntentFilter(IDataContants.RES_ACTION);
         //注册广播接受者
         mIDataReceiver = new IDataReceiver(this);
         registerReceiver(mIDataReceiver, intentFilter);
    }

    /**
     * 初始化Supoin的扫描枪
     */
    void initSupoin(){
        IntentFilter intentFilter = new IntentFilter(SupoinConstants.SCN_CUST_ACTION_SCODE);
        supoinReceiver = new SupoinReceiver(this);
        registerReceiver(supoinReceiver, intentFilter);
    }
    /**
     * 初始化Seuic的扫描枪
     */
    void initSeuic(){
        Intent intent = new Intent(SeuicBroadcastConfig.BROADCAST_SETTING);
        // 修改广播名称
        intent.putExtra(SeuicBroadcastConfig.BROADCAST_KEY, SeuicBroadcastConfig.CUSTOM_NAME);
        // 设置条码发送方式为广播模式
        intent.putExtra(SeuicBroadcastConfig.SEND_KEY, "BROADCAST");
        // 设置条码结束符为none
        intent.putExtra(SeuicBroadcastConfig.END_KEY, "NONE");
        this.sendBroadcast(intent);

        IntentFilter filter = new IntentFilter(SeuicBroadcastConfig.CUSTOM_NAME);
        seuicReceiver = new SeuicReceiver(this);
        registerReceiver(seuicReceiver, filter);
        SeuicSettingHelp.setSound(this, true);
    }

    @Override
    public void seuicReceiveHandle(String result) {
        handle(result);
    }

    @Override
    public void supoinReceiveHandle(String result) {
        handle(result);
    }

    @Override
    public void IDataReceiveHandle(String result) {
        handle(result);
    }

    public void handle(String result){
        if (isCanScan)
            if (!PreferencesUtils.getBoolean(getApplicationContext(),IS_REPEAT_CODE))
                if (scanBackCallBack != null)
                    scanBackCallBack.scanBackHandleDo(result);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(supoinReceiver);
        unregisterReceiver(seuicReceiver);
        unregisterReceiver(mIDataReceiver);
    }



    public interface ScanBackCallBack{
        public void scanBackHandleDo(String scanResult);
    }

    public void setScanBackCallBack(ScanBackCallBack scanBackCallBack){
        this.scanBackCallBack = scanBackCallBack;
    }
    @Override
    protected void onPause() {
        super.onPause();
        isCanScan = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recoveryScan) {
            isCanScan = true;
        }
    }
}
