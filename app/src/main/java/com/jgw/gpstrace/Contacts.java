package com.jgw.gpstrace;

/**
 * Created by user on 2018/4/3.
 */

public class Contacts {


    //登记
    public static final String DENG_JI = "船只登记";
    //离港
    public static final String LI_GANG = "离港";
    //捕捞
    public static final String BU_LAO = "捕捞";
    //检测
    public static final String CHECK = "检测";
    //入港
    public static final String RU_GANG = "入港";

    //登记
    public static final String[] DENG_JIS = {"船只名称", "负责人", "服役时间"};
    //离港
    public static final String[] LI_GANGS = {"时间", "目标海域", "定位"};
    //捕捞
    public static final String[] BU_LAOS = {"时间", "海域", "方式", "捕获品种", "捕获量", "定位"};
    //检测
    public static final String[] CHECKS = {"时间", "人员", "检测项", "结果"};
    //入港
    public static final String[] RU_GANGS = {"时间", "定位"};


    public static final String LOCATION = "Location";


    public static final String[] PARENT_NODE = {DENG_JI, LI_GANG, BU_LAO, CHECK, RU_GANG};


}
