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
import com.owl.chatstory.data.homesource.model.CategoryModel;
import com.owl.chatstory.home.HomeContract;
import com.owl.chatstory.home.HomePresenter;
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

public class ChooseCategoryActivity extends BaseActivity implements HomeContract.View {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.choose_category_recycler)
    RecyclerView mRecyclerView;

    private CommonAdapter<CategoryModel> mAdapter;
    private List<CategoryModel> mDatas = new ArrayList<>();
    private HomeContract.Presenter mPresenter;


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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
    }

    @Override
    protected void initViewsAndData() {
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
        new HomePresenter(this);
    }

    @Override
    public void showCategoryList(List<CategoryModel> list) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getCreateCategoryList();
    }
}
