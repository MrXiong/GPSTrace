package com.jgw.gpstrace.scan.idata;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by admin on 2017/4/18.
 */

public class IDataReceiver extends BroadcastReceiver {
    IDataReceiverService mIDataReceiverService;
    public IDataReceiver() {
        super();
    }
    public IDataReceiver(IDataReceiverService iDataReceiverService) {
        super();
        this.mIDataReceiverService = iDataReceiverService;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(IDataContants.RES_ACTION)){
            String resultCode = intent.getStringExtra("value");
            mIDataReceiverService.IDataReceiveHandle(resultCode);
        }

    }
}
