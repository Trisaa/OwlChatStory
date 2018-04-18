package com.tap.chatstory.user.info;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.tap.chatstory.R;
import com.tap.chatstory.base.BaseActivity;
import com.tap.chatstory.data.usersource.model.WalletModel;

import butterknife.BindView;

/**
 * Created by lebron on 2018/4/3.
 */

public class WalletActivity extends BaseActivity implements WalletContract.View {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.gift_income_txt)
    TextView mCoinView;

    private WalletContract.Presenter mPresenter;
    private ValueAnimator mAnimator;

    public static void start(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_wallet;
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
        new WalletPresenter(this);
    }

    @Override
    public void showCoins(WalletModel model) {
        textAnim(model.getCoin());
    }

    @Override
    public void setPresenter(WalletContract.Presenter presenter) {
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
                mCoinView.setText(String.valueOf(value));
            }
        });
        mAnimator.start();
    }
}
