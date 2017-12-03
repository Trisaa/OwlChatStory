package com.owl.chatstory.creation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.chat.adapter.ChatAsideDelegate;
import com.owl.chatstory.chat.adapter.ChatLeftDelegate;
import com.owl.chatstory.chat.adapter.ChatNextDelegate;
import com.owl.chatstory.chat.adapter.ChatRightDelegate;
import com.owl.chatstory.chat.adapter.ReadItemDecoration;
import com.owl.chatstory.common.view.SpaceRecyclerView;
import com.owl.chatstory.data.chatsource.model.MessageModel;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 2017/11/12.
 */

public class PreviewActivity extends BaseActivity {
    private static final String EXTRA_MESSAGE_LIST = "EXTRA_MESSAGE_LIST";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.read_recyclerview)
    SpaceRecyclerView mRecyclerView;
    @BindView(R.id.read_appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.read_progressbar)
    ProgressBar mProgressbar;
    @BindView(R.id.read_click_tips_txv)
    View mClickTipsView;

    private MultiItemTypeAdapter<MessageModel> mAdapter;
    private List<MessageModel> mDatas;
    private List<MessageModel> mShowDatas = new ArrayList<>();

    public static void start(Context context, ArrayList<MessageModel> list) {
        Intent intent = new Intent(context, PreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_MESSAGE_LIST, list);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private ChatNextDelegate.OnClickListener mNextListener = new ChatNextDelegate.OnClickListener() {
        @Override
        public void onClick() {
            clickNext();
        }

        @Override
        public void onShareClick() {
        }

        @Override
        public void onLongClick() {
        }

        @Override
        public void onNextClick() {
        }
    };

    @Override
    protected int getContentViewID() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initToolBar() {
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.common_preview);
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void initViewsAndData() {
        mDatas = getIntent().getParcelableArrayListExtra(EXTRA_MESSAGE_LIST);
        MessageModel messageModel = new MessageModel();
        messageModel.setLocation("end");
        mShowDatas.add(mDatas.get(0));
        mShowDatas.add(messageModel);

        mAdapter = new MultiItemTypeAdapter<>(this, mShowDatas);
        mAdapter.addItemViewDelegate(new ChatLeftDelegate());
        mAdapter.addItemViewDelegate(new ChatRightDelegate());
        mAdapter.addItemViewDelegate(new ChatAsideDelegate());
        mAdapter.addItemViewDelegate(new ChatNextDelegate(mNextListener));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ReadItemDecoration(24));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setBlankListener(new SpaceRecyclerView.BlankListener() {
            @Override
            public void onBlankClick() {
                clickNext();
            }
        });
    }

    @OnClick(R.id.read_click_tips_txv)
    public void clickTips() {
        mClickTipsView.setVisibility(View.GONE);
        clickNext();
    }

    private void clickNext() {
        if (mDatas != null && mShowDatas.size() > 0) {
            int i = mShowDatas.size() - 1;
            mProgressbar.setProgress(100 * i / mDatas.size());
            if (i < mDatas.size()) {
                mShowDatas.add(i, mDatas.get(i));
                mAdapter.notifyItemInserted(i);
                mRecyclerView.smoothScrollToPosition(i + 1);
                mAppBarLayout.setExpanded(false);
            }
        }
    }
}
