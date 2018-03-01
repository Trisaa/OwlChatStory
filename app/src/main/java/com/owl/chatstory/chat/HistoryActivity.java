package com.owl.chatstory.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.CommonVerticalItemDecoration;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.util.network.request.FictionListRequest;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lebron on 2017/9/14.
 */

public class HistoryActivity extends BaseActivity implements HistoryContract.View, LoadMoreWrapper.OnLoadMoreListener {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.history_recyclerview)
    RecyclerView mRecyclerView;

    private LoadMoreWrapper<FictionDetailModel> mLoadMoreWrapper;
    private List<FictionDetailModel> mDatas = new ArrayList<>();
    private HistoryContract.Presenter mPresenter;
    private FictionListRequest mRequest;
    private View mLoadMoreView, mEmptyView;
    private int mPage = 1, mPrePage;

    public static void start(Context context) {
        Intent intent = new Intent(context, HistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_history;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            DialogUtils.showDialog(this, R.string.common_delete, R.string.common_cancel, R.string.common_ok
                    , new DialogUtils.OnDialogClickListener() {
                        @Override
                        public void onOK() {
                            if (mPresenter != null) {
                                mPresenter.clearHistory();
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewsAndData() {
        CommonAdapter<FictionDetailModel> mAdapter = new CommonAdapter<FictionDetailModel>(this, R.layout.story_category_item, mDatas) {
            @Override
            protected void convert(ViewHolder holder, FictionDetailModel fictionModel, int position) {
                ImageLoaderUtils.getInstance().loadImage(HistoryActivity.this, fictionModel.getCover(), (ImageView) holder.getView(R.id.category_item_cover_img), R.color.colorPrimaryDark);
                holder.setText(R.id.category_item_title_txv, fictionModel.getTitle());
                holder.setText(R.id.category_item_description_txv, fictionModel.getSummary());
                holder.setText(R.id.category_item_watchers_txv, fictionModel.getViews() + "");
                holder.setText(R.id.category_upinfo_txv, getString(R.string.common_update_chapter, fictionModel.getUpinfo()));
                holder.setVisible(R.id.category_item_vip_img, fictionModel.getVip() == 0 ? false : true);
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ReadActivity.start(HistoryActivity.this, mDatas.get(position).getId(), mDatas.get(position).getTitle());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        EmptyWrapper<FictionDetailModel> emptyWrapper = new EmptyWrapper<>(mAdapter);
        mEmptyView = LayoutInflater.from(this).inflate(R.layout.common_empty_layout, null);
        mEmptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        emptyWrapper.setEmptyView(mEmptyView);
        mLoadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.common_loading_more_layout, null);
        mLoadMoreView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        mLoadMoreWrapper.setLoadMoreView(mLoadMoreView);
        mLoadMoreWrapper.setOnLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new CommonVerticalItemDecoration(32));
        mRecyclerView.setAdapter(mLoadMoreWrapper);

        new HistoryPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(HistoryContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getFictionList(getRequest(mPage), true);
    }

    @Override
    public void showHistoryList(List<FictionDetailModel> list, boolean refresh) {
        if (refresh) {
            if (mDatas != null) {
                mDatas.clear();
            }
        }
        if (list.size() > 0) {
            mLoadMoreView.findViewById(R.id.loading_more_layout).setVisibility(View.VISIBLE);
            mPage++;
        } else {
            mLoadMoreView.findViewById(R.id.loading_more_layout).setVisibility(View.GONE);
        }
        mDatas.addAll(list);
        mLoadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    public void showErrorView() {
        Toast.makeText(this, R.string.common_network_error, Toast.LENGTH_SHORT).show();
        ((TextView) mEmptyView.findViewById(R.id.empty_txv)).setText(R.string.common_network_error);
    }

    @Override
    public void clearHistory(boolean success) {
        if (success) {
            mDatas.clear();
            mLoadMoreWrapper.notifyDataSetChanged();
        } else {
            Toast.makeText(this, R.string.common_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private FictionListRequest getRequest(int page) {
        if (mRequest == null) {
            mRequest = new FictionListRequest();
        }
        mRequest.setToken(PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, ""));
        mRequest.setPage(page);
        mRequest.setCount(20);
        return mRequest;
    }

    @Override
    public void onLoadMoreRequested() {
        Log.i("Lebron", " onLoadMoreRequested " + mPage + " " + mPrePage);
        if (mPage != mPrePage) {
            mPresenter.getFictionList(getRequest(mPage), false);
            mPrePage = mPage;
        }
    }
}
