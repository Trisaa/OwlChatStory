package com.tap.chatstory.common.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lebron on 2017/7/13.
 */

public class CommonHorizontalItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public CommonHorizontalItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = space;
            outRect.right = space / 2;
        } else if (parent.getChildLayoutPosition(view) == state.getItemCount() - 1) {
            outRect.left = space / 2;
            outRect.right = space;
        } else {
            outRect.left = space / 2;
            outRect.right = space / 2;
        }
    }
}
