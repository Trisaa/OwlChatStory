package com.owl.chatstory.chat.adapter;

import com.owl.chatstory.R;
import com.owl.chatstory.data.chatsource.model.MessageModel;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lebron on 2017/9/13.
 */

public class ChatAsideDelegate implements ItemViewDelegate<MessageModel> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.read_aside_item;
    }

    @Override
    public boolean isForViewType(MessageModel item, int position) {
        return item.getLocation().equals("center");
    }

    @Override
    public void convert(ViewHolder holder, MessageModel messageModel, int position) {
        holder.setText(R.id.read_aside_txv, messageModel.getWord());
    }
}
