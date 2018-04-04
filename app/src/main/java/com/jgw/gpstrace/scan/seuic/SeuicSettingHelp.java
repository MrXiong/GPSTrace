package com.jgw.gpstrace.scan.seuic;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by linpg on 2016/10/24.
 */
public class SeuicSettingHelp {
    /**
     * 广播设置声音
     * @param context
     * @param isChecked
     */
    public static void setSound(final Context context, boolean isChecked){
        Intent intent = new Intent(SeuicBroadcastConfig.BROADCAST_SETTING);
        intent.putExtra(SeuicBroadcastConfig.SOUND_KEY, isChecked);
        context.sendBroadcast(intent);
    }

    /**
     * 广播设置振动
     * @param context
     * @param isChecked
     */
    public static void setVibrate(final Context context, boolean isChecked){
        Intent intent = new Intent(SeuicBroadcastConfig.BROADCAST_SETTING);
        intent.putExtra(SeuicBroadcastConfig.VIBERATE_KEY, isChecked);
        context.sendBroadcast(intent);
    }

    /**
     * * 广播设置连扫
    * @param context
    * @param isChecked
    */
    public static void setContinue(final Context context, boolean isChecked){
        Intent intent = new Intent(SeuicBroadcastConfig.BROADCAST_SETTING);
        intent.putExtra(SeuicBroadcastConfig.CONTINIU_KEY, isChecked);
        context.sendBroadcast(intent);
    }

    /**
     * 广播设置持续出光
     * @param context
     * @param chixuTime
     */
    public static void setChixu(final Context context, String chixuTime){
        Intent intent = new Intent(SeuicBroadcastConfig.ACTION_PARAM_SETTINGS);
        intent.putExtra("number", 0x01);
        intent.putExtra("value", Integer.parseInt(chixuTime));
        context.sendBroadcast(intent);
        Toast.makeText(context, "设置成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 广播设置连扫时间
     * @param context
     * @param chixuTime
     */
    public static void setContinueTime(final Context context, String chixuTime){
        Intent intent = new Intent(SeuicBroadcastConfig.BROADCAST_SETTING);
        intent.putExtra("interval", Integer.parseInt(chixuTime));
        context.sendBroadcast(intent);
        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
    }

    // 持续出光
    static boolean flag ;
    public static void setChixutime(final Context context, boolean isChecked){
        if (isChecked){
            Intent intent = new Intent(SeuicBroadcastConfig.SCAN_START);
            context.sendBroadcast(intent);
        }else{
            Intent intent2 = new Intent(SeuicBroadcastConfig.SCAN_STOP);
            context.sendBroadcast(intent2);
        }
    }
}
