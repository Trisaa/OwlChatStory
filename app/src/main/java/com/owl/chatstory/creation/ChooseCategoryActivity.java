package com.owl.chatstory.creation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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

    private List<String> mDatas;
    private CommonAdapter<String> mAdapter;


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
        mDatas = new ArrayList<>();
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mAdapter = new CommonAdapter<String>(this, R.layout.category_choose_item, mDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
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
