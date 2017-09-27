package com.owl.chatstory.chat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.chat.adapter.ChatAsideDelegate;
import com.owl.chatstory.chat.adapter.ChatLeftDelegate;
import com.owl.chatstory.chat.adapter.ChatNextDelegate;
import com.owl.chatstory.chat.adapter.ChatRightDelegate;
import com.owl.chatstory.common.util.CommonVerticalItemDecoration;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.view.SpaceRecyclerView;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.MessageModel;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lebron on 2017/9/12.
 */

public class ReadActivity extends BaseActivity implements ReadContract.View {
    private static final String EXTRA_FICTION_ID = "EXTRA_FICTION_ID";
    private static final String EXTRA_FICTION_NAME = "EXTRA_FICTION_NAME";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.read_recyclerview)
    SpaceRecyclerView mRecyclerView;
    @BindView(R.id.read_appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.read_progressbar)
    ProgressBar mProgressbar;
    @BindView(R.id.read_loading_progressbar)
    ProgressBar mLoadingBar;

    private ReadContract.Presenter mPresenter;
    private MultiItemTypeAdapter<MessageModel> mAdapter;
    private List<MessageModel> mDatas;
    private List<MessageModel> mShowDatas = new ArrayList<>();

    public static void start(Context context, String fictionId, String fictionName) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(EXTRA_FICTION_ID, fictionId);
        intent.putExtra(EXTRA_FICTION_NAME, fictionName);
        context.startActivity(intent);
    }

    private ChatNextDelegate.OnClickListener mNextListener = new ChatNextDelegate.OnClickListener() {
        @Override
        public void onClick() {
            clickNext();
        }

        @Override
        public void onShareClick() {
            DialogUtils.showShareDialog(ReadActivity.this);
        }
    };

    @Override
    protected int getContentViewID() {
        return R.layout.activity_read;
    }

    @Override
    protected void initToolBar() {
        if (mToolbar != null) {
            mToolbar.setTitle(getIntent().getStringExtra(EXTRA_FICTION_NAME));
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void initViewsAndData() {
        mAdapter = new MultiItemTypeAdapter<>(this, mShowDatas);
        mAdapter.addItemViewDelegate(new ChatLeftDelegate());
        mAdapter.addItemViewDelegate(new ChatRightDelegate());
        mAdapter.addItemViewDelegate(new ChatAsideDelegate());
        mAdapter.addItemViewDelegate(new ChatNextDelegate(mNextListener));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new CommonVerticalItemDecoration(24));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setBlankListener(new SpaceRecyclerView.BlankListener() {
            @Override
            public void onBlankClick() {
                clickNext();
            }
        });
        new ReadPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.read_menu_share:
                DialogUtils.showShareDialog(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clickNext() {
        int i = mShowDatas.size() - 1;
        Log.i("Lebron", " onclick " + i);
        mProgressbar.setProgress(100 * i / mDatas.size());
        if (i < mDatas.size()) {
            mShowDatas.add(i, mDatas.get(i));
            mAdapter.notifyItemInserted(i);
            mRecyclerView.smoothScrollToPosition(i + 1);
            mAppBarLayout.setExpanded(false);
        } else {
            mShowDatas.get(i).setEnded(true);
            mAdapter.notifyItemChanged(i);
            mPresenter.addToHistory(getIntent().getStringExtra(EXTRA_FICTION_ID));
        }
    }

    @Override
    public void showFictionData(FictionModel model) {
        mLoadingBar.setVisibility(View.GONE);
        mDatas = model.getList();
        MessageModel messageModel = new MessageModel();
        messageModel.setLocation("end");
        mShowDatas.add(mDatas.get(0));
        mShowDatas.add(messageModel);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(ReadContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getFictionData(getIntent().getStringExtra(EXTRA_FICTION_ID));
    }
}
