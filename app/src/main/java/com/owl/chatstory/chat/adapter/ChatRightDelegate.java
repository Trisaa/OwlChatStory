package com.owl.chatstory.chat.adapter;

import android.widget.ImageView;

import com.owl.chatstory.MainApplication;
import com.owl.chatstory.R;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.data.chatsource.model.MessageModel;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lebron on 2017/9/13.
 */

public class ChatRightDelegate implements ItemViewDelegate<MessageModel> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.read_right_item;
    }

    @Override
    public boolean isForViewType(MessageModel item, int position) {
        return item.getLocation().equals("right");
    }

    @Override
    public void convert(ViewHolder holder, MessageModel model, int position) {
        ImageLoaderUtils.getInstance().loadCircleImage(MainApplication.getAppContext(), model.getAvatar()
                , (ImageView) holder.getView(R.id.read_right_icon));
        holder.setText(R.id.read_right_name, model.getActor());
        holder.setText(R.id.read_right_content, model.getWord());
    }
}
