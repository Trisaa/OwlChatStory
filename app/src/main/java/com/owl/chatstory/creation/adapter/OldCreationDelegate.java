package com.owl.chatstory.creation.adapter;

import android.view.View;

import com.owl.chatstory.R;
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
    public void convert(ViewHolder holder, FictionDetailModel model, int position) {
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
