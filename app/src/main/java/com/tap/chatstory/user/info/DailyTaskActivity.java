package com.tap.chatstory.user.info;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.tap.chatstory.R;
import com.tap.chatstory.base.BaseActivity;
import com.tap.chatstory.common.util.Constants;
import com.tap.chatstory.common.util.ShareUtils;
import com.tap.chatstory.creation.BasicCreateActivity;
import com.tap.chatstory.data.homesource.model.ShareModel;
import com.tap.chatstory.data.usersource.ICoinDataImpl;
import com.tap.chatstory.data.usersource.model.TaskModel;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lebron on 2018/4/16.
 */

public class DailyTaskActivity extends BaseActivity implements DailyTaskContract.View {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.history_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.gift_income_txt)
    TextView mCoinsView;

    private DailyTaskContract.Presenter mPresenter;
    private CallbackManager mCallbackManager;
    private CommonAdapter<TaskModel.TaskDetail> mAdapter;
    private List<TaskModel.TaskDetail> mDatas = new ArrayList<>();
    private ValueAnimator mAnimator;

    public static void start(Context context) {
        Intent intent = new Intent(context, DailyTaskActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_daily_task;
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
        mCallbackManager = CallbackManager.Factory.create();
        final HashMap<String, Integer> hashMap = new HashMap();
        hashMap.put(ICoinDataImpl.REWARDS_CREATED, R.string.daily_task_create);
        hashMap.put(ICoinDataImpl.REWARDS_LOGINED, R.string.daily_task_login);
        hashMap.put(ICoinDataImpl.REWARDS_SHARED, R.string.daily_task_share);
        hashMap.put(ICoinDataImpl.REWARDS_READED, R.string.daily_task_read);
        mAdapter = new CommonAdapter<TaskModel.TaskDetail>(this, R.layout.daily_task_item, mDatas) {
            @Override
            protected void convert(ViewHolder holder, final TaskModel.TaskDetail taskDetail, int position) {
                holder.setText(R.id.daily_task_name, getString(hashMap.get(taskDetail.getTaskName())));
                TextView finishView = holder.getView(R.id.daily_task_finish);
                if (taskDetail.getFinished() == 1) {
                    finishView.setText(R.string.common_finished);
                    finishView.setEnabled(false);
                } else {
                    finishView.setText("+" + taskDetail.getReward());
                    finishView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (taskDetail.getTaskName()) {
                                case ICoinDataImpl.REWARDS_CREATED:
                                    BasicCreateActivity.start(DailyTaskActivity.this, null);
                                    finish();
                                    break;
                                case ICoinDataImpl.REWARDS_LOGINED:
                                    mPresenter.getRewards(ICoinDataImpl.REWARDS_LOGINED);
                                    break;
                                case ICoinDataImpl.REWARDS_SHARED:
                                    ShareModel shareModel = new ShareModel();
                                    shareModel.setContent(getString(R.string.share_content));
                                    shareModel.setUrl(ShareUtils.getShareAppUrl(DailyTaskActivity.this));
                                    ShareUtils.shareToFacebook(DailyTaskActivity.this, mCallbackManager, shareModel, new ShareUtils.OnShareListener() {
                                        @Override
                                        public void onShare(boolean success) {
                                            if (success) {
                                                mPresenter.getRewards(ICoinDataImpl.REWARDS_SHARED);
                                            }
                                        }
                                    });
                                    break;
                                case ICoinDataImpl.REWARDS_READED:
                                    EventBus.getDefault().post(Constants.EVENT_GOTO_READACTIVITY);
                                    finish();
                                    break;
                            }
                        }
                    });
                }
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        new DailyTaskPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showTaskList(TaskModel taskModel) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(taskModel.getTaskList());
        textAnim(taskModel.getCoins());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRewards() {
        mPresenter.subscribe();
    }

    @Override
    public void setPresenter(DailyTaskContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
        if (mAnimator != null & mAnimator.isRunning()) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    private void textAnim(int coins) {
        mAnimator = ValueAnimator.ofInt(0, coins);
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                mCoinsView.setText(String.valueOf(value));
            }
        });
        mAnimator.start();
    }
}
