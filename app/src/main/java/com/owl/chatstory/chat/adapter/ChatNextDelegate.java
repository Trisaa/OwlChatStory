package com.owl.chatstory.chat.adapter;

import android.view.View;

import com.owl.chatstory.R;
import com.owl.chatstory.data.chatsource.model.MessageModel;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lebron on 2017/9/13.
 */

public class ChatNextDelegate implements ItemViewDelegate<MessageModel> {
    private OnClickListener mListener;

    public ChatNextDelegate(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.read_next_item;
    }

    @Override
    public boolean isForViewType(MessageModel item, int position) {
        return item.getLocation().equals("end");
    }

    @Override
    public void convert(ViewHolder holder, MessageModel messageModel, int position) {
        holder.setVisible(R.id.read_over_layout, messageModel.isEnded());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick();
                }
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onLongClick();
                }
                return false;
            }
        });
        holder.setOnClickListener(R.id.read_share_txv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onShareClick();
                }
            }
        });
    }

    public interface OnClickListener {
        void onClick();

        void onShareClick();

        void onLongClick();
    }
}
