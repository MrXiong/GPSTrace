package com.jgw.gpstrace.scan.seuic;

/**
 * Created by linpg on 2016/10/25.
 */
public class SeuicBroadcastConfig {
    // ScannerService里面  “应用设置”  的广播接收器ACTION
    public final static String BROADCAST_SETTING = "com.android.scanner.service_settings";

    // ScannerService的条码广播设置 key 值
    public final static String BROADCAST_KEY = "action_barcode_broadcast";

    // ScannerService的广播接收器value值(自定义)
    public final static String CUSTOM_NAME = "com.example.chinaautoid";

    // ScannerService的条码发送方式Key
    public final static String SEND_KEY = "barcode_send_mode";

    // ScannerService的条码结束符设置
    public final static String END_KEY = "endchar";

    // ScannerService声音 key 值
    public final static String SOUND_KEY = "sound_play";

    // ScannerService振动 key 值
    public final static String VIBERATE_KEY = "viberate";

    // ScannerService连续扫描 key 值
    public final static String CONTINIU_KEY = "scan_continue";

    // 开始扫描
    public final static String SCAN_START = "com.scan.onStartScan";

    // 停止扫描
    public final static String SCAN_STOP = "com.scan.onEndScan";

    //扫描灯的控制
    public final static String SCAN_LIGHT = "com.android.scanner.ENABLED";

    // ScannerService里面  “条码设置”  广播接收器ACTION
    public final static String ACTION_PARAM_SETTINGS = "com.seuic.scanner.action.PARAM_SETTINGS";
}
