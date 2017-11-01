package com.owl.chatstory.chat.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lebron on 2017/11/1.
 */

public class ReadItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ReadItemDecoration(int space) {
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
            outRect.bottom = 0;
        } else {
            outRect.top = space / 2;
            outRect.bottom = space / 2;
        }
    }
}
