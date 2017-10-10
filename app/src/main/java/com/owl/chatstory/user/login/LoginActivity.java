package com.owl.chatstory.user.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.JumpUtils;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.view.CustomTwitterLoginButton;
import com.owl.chatstory.data.usersource.model.UserModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.owl.chatstory.common.util.JumpUtils.PRIVACY_POLICY_URL;

/**
 * Created by lebron on 2017/9/4.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {
    public static final String LOGIN_EVENT = "LOGIN_EVENT";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.login_twitter_btn)
    CustomTwitterLoginButton mTwitterButton;
    @BindView(R.id.common_progressbar)
    ProgressBar mProgressBar;

    private LoginContract.Presenter mPresenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_login;
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
        new LoginPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @OnClick(R.id.login_facebook_btn)
    public void clickFacebook() {
        mPresenter.signinWithFacebook(this);
    }

    @OnClick(R.id.login_twitter_btn)
    public void clickTwitter() {
        mPresenter.signinWithTwitter(mTwitterButton);
    }

    @OnClick(R.id.login_privacy_txv)
    public void clickPrivacy() {
        JumpUtils.jumpToBrowser(PRIVACY_POLICY_URL);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void loginResult(UserModel userModel) {
        if (userModel != null) {
            PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_USER_ID, userModel.getId());
            PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_USER, new Gson().toJson(userModel));
            EventBus.getDefault().post(userModel);
            finish();
        } else {
            Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void showProgressBar(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
