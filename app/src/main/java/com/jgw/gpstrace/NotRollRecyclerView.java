package com.jgw.gpstrace;

import android.content.Context;
import android.util.AttributeSet;

import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * Created by zx on 2016/7/26.
 */
public class NotRollRecyclerView extends RecyclerViewFinal {
    public NotRollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
