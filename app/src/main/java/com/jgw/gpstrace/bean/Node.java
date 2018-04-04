package com.jgw.gpstrace.bean;

import com.baidu.location.BDLocation;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2018/4/3.
 */

public class Node implements Serializable {
    public static final String NODE = "node";

    //位置
    private double latitude;
    private double longitude;
    private String nodeName;
    private String[] nodes;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String[] getNodes() {
        return nodes;
    }

    public void setNodes(String[] nodes) {
        this.nodes = nodes;
    }
}
