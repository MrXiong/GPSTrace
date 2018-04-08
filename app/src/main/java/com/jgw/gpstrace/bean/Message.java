package com.jgw.gpstrace.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/4/3.
 */

public class Message extends DataSupport implements Serializable {
    public static final String VALUE_LIST = "VALUE_LIST";
    private int id;
    private String key;
    private String valueHint;
    private String value;
    private List<Code> codeList = new ArrayList<>();

    public List<Code> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Code> codeList) {
        this.codeList = codeList;
    }

    public String getValueHint() {
        return valueHint;
    }

    public void setValueHint(String valueHint) {
        this.valueHint = valueHint;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
