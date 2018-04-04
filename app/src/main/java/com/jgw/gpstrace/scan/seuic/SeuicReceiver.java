package com.jgw.gpstrace.scan.seuic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by linpg on 2016/10/24.
 */
public class SeuicReceiver extends BroadcastReceiver {
    SeuicReceiverService seuicReceiverService;
    public SeuicReceiver(){
        super();
    }
    public SeuicReceiver(SeuicReceiverService seuicReceiverService){
        super();
        this.seuicReceiverService = seuicReceiverService;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String barcode = intent.getStringExtra("scannerdata");
        seuicReceiverService.seuicReceiveHandle(barcode);
    }
}
