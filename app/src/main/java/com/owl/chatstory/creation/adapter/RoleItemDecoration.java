package com.owl.chatstory.creation.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lebron on 2017/11/7.
 */

public class RoleItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public RoleItemDecoration(int space) {
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
            outRect.right = 0;
        } else {
            outRect.left = space / 2;
            outRect.right = space / 2;
        }
    }
}
