package com.owl.chatstory.creation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.data.homesource.model.CategoryModel;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lebron on 2017/11/2.
 */

public class ChooseCategoryActivity extends BaseActivity {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.choose_category_recycler)
    RecyclerView mRecyclerView;

    private CommonAdapter<CategoryModel> mAdapter;
    private List<CategoryModel> mDatas;


    public static void start(Context context) {
        Intent intent = new Intent(context, ChooseCategoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_choose_category;
    }

    @Override
    protected void initToolBar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void initViewsAndData() {
        String string = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_CATEGORY_LIST, "");
        mDatas = new Gson().fromJson(string, new TypeToken<List<CategoryModel>>() {
        }.getType());
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mAdapter = new CommonAdapter<CategoryModel>(this, R.layout.category_choose_item, mDatas) {
            @Override
            protected void convert(ViewHolder holder, CategoryModel model, int position) {
                holder.setText(R.id.category_choose_item_txv, model.getTitle());
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EventBus.getDefault().post(mDatas.get(position));
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setAdapter(mAdapter);
    }
}
