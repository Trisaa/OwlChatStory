package com.owl.chatstory.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.owl.chatstory.common.util.GlobalSize;

/**
 * Created by lebron on 2017/11/7.
 */

public class MaxWidthRecyclerView extends RecyclerView {
    public MaxWidthRecyclerView(Context context) {
        this(context, null);
    }

    public MaxWidthRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MaxWidthRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        try {
            int screenWidth = GlobalSize.getScreenWidth(getContext());
            int width = screenWidth - GlobalSize.dip2px(getContext(), 180);
            widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
        } catch (Exception e) {
        }
        super.onMeasure(widthSpec, heightSpec);
    }
}
