package com.tap.chatstory.common.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lebron on 2017/7/13.
 */

public class CommonVerticalItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public CommonVerticalItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
            outRect.bottom = space / 2;
        } else if (parent.getChildLayoutPosition(view) == state.getItemCount() - 1) {
            outRect.top = space / 2;
            outRect.bottom = space;
        } else {
            outRect.top = space / 2;
            outRect.bottom = space / 2;
        }
    }
}
