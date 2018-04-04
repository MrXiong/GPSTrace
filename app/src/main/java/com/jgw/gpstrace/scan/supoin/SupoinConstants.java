package com.jgw.gpstrace.scan.supoin;

import android.widget.ArrayAdapter;

/**
 * Created by linpg on 2016/10/24.
 */
public class SupoinConstants {
    public static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    public static final String SCN_CUST_EX_SCODE = "scannerdata";
    /* defined by MEXXEN */
    public static final String SCN_CUST_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";
    public static final String SCN_CUST_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";

    private static final String SCANNER_CTRL_FILE = "/sys/devices/platform/scan_se955/se955_state";
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    public static final String SCANNER_OUTPUT_MODE = "SCANNER_OUTPUT_MODE";
}
