package com.jgw.gpstrace;

import java.util.List;

/**
 * Created by user on 2018/4/3.
 */

public class ListUtils {
    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }
}
