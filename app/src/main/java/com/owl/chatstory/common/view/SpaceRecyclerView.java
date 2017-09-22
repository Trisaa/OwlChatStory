package com.owl.chatstory.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lebron on 2017/9/13.
 */

public class SpaceRecyclerView extends RecyclerView {
    private GestureDetectorCompat mGestureDetector;
    private BlankListener mListener;

    public SpaceRecyclerView(Context context) {
        this(context, null);
    }

    public SpaceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SpaceRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBlankListener(BlankListener listener) {
        this.mListener = listener;
        this.mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mGestureDetector.onTouchEvent(e)) {
            View childView = findChildViewUnder(e.getX(), e.getY());
            if (childView == null) {
                mListener.onBlankClick();
                return true;
            }
        }
        return super.onTouchEvent(e);
    }

    public interface BlankListener {
        void onBlankClick();
    }
}
