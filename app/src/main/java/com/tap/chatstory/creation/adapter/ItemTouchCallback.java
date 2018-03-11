package com.tap.chatstory.creation.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by lebron on 2017/11/18.
 */

public class ItemTouchCallback extends ItemTouchHelper.Callback {
    private ItemTouchListener mListener;

    public ItemTouchCallback(ItemTouchListener listener) {
        mListener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (mListener != null) {
            mListener.onItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mListener != null) {
            mListener.onItemDismissd(viewHolder.getAdapterPosition());
        }
    }

    public interface ItemTouchListener {

        void onItemMoved(int fromPosition, int toPosition);

        void onItemDismissd(int position);
    }
}
