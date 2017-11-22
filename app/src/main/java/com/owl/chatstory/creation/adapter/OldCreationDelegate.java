package com.owl.chatstory.creation.adapter;

import android.view.View;
import android.widget.ImageView;

import com.owl.chatstory.R;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.util.TimeUtils;
import com.owl.chatstory.creation.CreationDetailActivity;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lebron on 2017/10/31.
 */

public class OldCreationDelegate implements ItemViewDelegate<FictionDetailModel> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.my_creation_item;
    }

    @Override
    public boolean isForViewType(FictionDetailModel item, int position) {
        return true;
    }

    @Override
    public void convert(final ViewHolder holder, final FictionDetailModel model, int position) {
        holder.setText(R.id.my_creation_title_txv, model.getTitle());
        holder.setText(R.id.my_creation_time_txv, TimeUtils.getTimeFormat(model.getCreateLine()));
        ImageLoaderUtils.getInstance().loadImage(holder.getConvertView().getContext(), model.getCover(), (ImageView) holder.getView(R.id.my_creation_img), R.color.colorPrimaryDark);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreationDetailActivity.start(holder.getConvertView().getContext(), model.getId());
            }
        });
    }
}
