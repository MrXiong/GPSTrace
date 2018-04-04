package com.jgw.gpstrace.scan.supoin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by linpg on 2016/10/24.
 */
public class SupoinReceiver extends BroadcastReceiver {
    SupoinReceiverService supoinReceiverService;
    public SupoinReceiver(){
        super();
    }
    public SupoinReceiver(SupoinReceiverService supoinReceiverService){
        super();
        this.supoinReceiverService = supoinReceiverService;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SupoinConstants.SCN_CUST_ACTION_SCODE)) {
            String message = intent.getStringExtra(SupoinConstants.SCN_CUST_EX_SCODE);
            supoinReceiverService.supoinReceiveHandle(message);
        }
    }
}
