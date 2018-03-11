package com.tap.chatstory.user.message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tap.chatstory.R;
import com.tap.chatstory.base.BaseActivity;
import com.tap.chatstory.common.util.Constants;
import com.tap.chatstory.common.util.DeviceUtils;
import com.tap.chatstory.common.util.ImageLoaderUtils;
import com.tap.chatstory.common.util.TimeUtils;
import com.tap.chatstory.data.usersource.model.MessagesModel;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lebron on 2018/3/2.
 */

public class MessageActivity extends BaseActivity implements MessageContract.View, LoadMoreWrapper.OnLoadMoreListener {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.history_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.common_progressbar_layout)
    View mLoadingView;
    @BindView(R.id.message_detail_layout)
    View mMessageDetailView;
    @BindView(R.id.message_detail_txv)
    TextView mMessageDetailText;

    private LoadMoreWrapper<MessagesModel> mLoadMoreWrapper;
    private List<MessagesModel> mDatas = new ArrayList<>();
    private View mLoadMoreView, mEmptyView;
    private int mPage = 1, mPrePage;
    private MessageContract.Presenter mPresenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_message;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    protected void initViewsAndData() {
        CommonAdapter<MessagesModel> mAdapter = new CommonAdapter<MessagesModel>(this, R.layout.message_item_layout, mDatas) {
            @Override
            protected void convert(ViewHolder holder, MessagesModel model, int position) {
                holder.setText(R.id.message_item_time_txv, TimeUtils.getTimeFormat(model.getCreateLine()));
                ImageLoaderUtils.getInstance().loadCircleImage(MessageActivity.this, DeviceUtils.getUri(R.mipmap.ic_launcher), (ImageView) holder.getView(R.id.message_item_icon));
                holder.getConvertView().setAlpha(model.getUnread() == 0 ? 0.5f : 1f);
                switch (model.getType()) {
                    case Constants.MESSAGE_LIKE:
                        holder.setText(R.id.message_item_content_txv, getString(R.string.message_liked,
                                model.getContent().getUserInfo().getUserName(), model.getContent().getFictionInfo().getTitle()));
                        break;
                    case Constants.MESSAGE_ONLINE:
                        holder.setText(R.id.message_item_content_txv, getString(R.string.message_online,
                                model.getContent().getFictionInfo().getTitle(),
                                model.getContent().getChapterInfo().getNum(),
                                model.getContent().getChapterInfo().getName()));
                        break;
                    case Constants.MESSAGE_VIOLATION:
                        holder.setText(R.id.message_item_content_txv, getString(R.string.message_rejected,
                                model.getContent().getFictionInfo().getTitle(),
                                model.getContent().getChapterInfo().getNum(),
                                model.getContent().getChapterInfo().getName(),
                                model.getContent().getReason()));
                        break;
                    case Constants.MESSAGE_PARY_UPDATE:
                        holder.setText(R.id.message_item_content_txv, getString(R.string.message_pray_update,
                                model.getContent().getUserInfo().getUserName(),
                                model.getContent().getFictionInfo().getTitle()));
                        break;
                    case Constants.MESSAGE_STAR:
                        holder.setText(R.id.message_item_content_txv, getString(R.string.message_stared,
                                model.getContent().getFictionInfo().getTitle(),
                                model.getContent().getChapterInfo().getNum(),
                                model.getContent().getChapterInfo().getName()));
                        break;
                }
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mMessageDetailView.setVisibility(View.VISIBLE);
                MessagesModel model = mDatas.get(position);
                switch (model.getType()) {
                    case Constants.MESSAGE_LIKE:
                        mMessageDetailText.setText(getString(R.string.message_liked,
                                model.getContent().getUserInfo().getUserName(), model.getContent().getFictionInfo().getTitle()));
                        break;
                    case Constants.MESSAGE_ONLINE:
                        mMessageDetailText.setText(getString(R.string.message_online,
                                model.getContent().getFictionInfo().getTitle(),
                                model.getContent().getChapterInfo().getNum(),
                                model.getContent().getChapterInfo().getName()));
                        break;
                    case Constants.MESSAGE_VIOLATION:
                        mMessageDetailText.setText(getString(R.string.message_rejected,
                                model.getContent().getFictionInfo().getTitle(),
                                model.getContent().getChapterInfo().getNum(),
                                model.getContent().getChapterInfo().getName(),
                                model.getContent().getReason()));
                        break;
                    case Constants.MESSAGE_PARY_UPDATE:
                        mMessageDetailText.setText(getString(R.string.message_pray_update,
                                model.getContent().getUserInfo().getUserName(),
                                model.getContent().getFictionInfo().getTitle()));
                        break;
                    case Constants.MESSAGE_STAR:
                        mMessageDetailText.setText(getString(R.string.message_stared,
                                model.getContent().getFictionInfo().getTitle(),
                                model.getContent().getChapterInfo().getNum(),
                                model.getContent().getChapterInfo().getName()));
                        break;
                }
                if (mPresenter != null) {
                    mPresenter.readMessage(model.getId());
                    model.setUnread(0);
                    view.setAlpha(model.getUnread() == 0 ? 0.5f : 1f);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        EmptyWrapper<MessagesModel> emptyWrapper = new EmptyWrapper<>(mAdapter);
        mEmptyView = LayoutInflater.from(this).inflate(R.layout.common_empty_layout, null);
        mEmptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        emptyWrapper.setEmptyView(mEmptyView);
        mLoadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.common_loading_more_layout, null);
        mLoadMoreView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        mLoadMoreWrapper.setLoadMoreView(mLoadMoreView);
        mLoadMoreWrapper.setOnLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.addItemDecoration(new CommonVerticalItemDecoration(32));
        mRecyclerView.setAdapter(mLoadMoreWrapper);
        new MessagePresenter(this);
    }

    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        mPresenter = presenter;
        mLoadingView.setVisibility(View.VISIBLE);
        mPresenter.getMessageList(mPage);
    }

    @Override
    public void showMessageList(List<MessagesModel> list) {
        mLoadingView.setVisibility(View.GONE);
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
    public void showMessageCount(int count) {

    }

    @Override
    public void onLoadMoreRequested() {
        Log.i("Lebron", " onLoadMoreRequested " + mPage + " " + mPrePage);
        if (mPage != mPrePage) {
            mPresenter.getMessageList(mPage);
            mPrePage = mPage;
        }
    }

    @Override
    public void onBackPressed() {
        if (mMessageDetailView.getVisibility() == View.VISIBLE) {
            mMessageDetailView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
