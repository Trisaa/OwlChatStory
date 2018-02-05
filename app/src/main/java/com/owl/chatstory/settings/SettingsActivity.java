package com.owl.chatstory.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.FeedbackUtils;
import com.owl.chatstory.common.util.JumpUtils;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.data.usersource.model.UserModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.owl.chatstory.common.util.JumpUtils.PRIVACY_POLICY_URL;

/**
 * Created by lebron on 2017/9/17.
 */

public class SettingsActivity extends BaseActivity {
    public static final String EVENT_LOG_OUT = "EVENT_LOG_OUT";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_settings;
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

    }

    @OnClick(R.id.settings_about_layout)
    public void clickAbout() {
        AboutActivity.start(this);
    }

    @OnClick(R.id.settings_feedback_layout)
    public void clickFeedback() {
        FeedbackUtils.startFeedbackActivity(this);
    }

    @OnClick(R.id.settings_logout_txv)
    public void clickLogout() {
        PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_USER_ID, "");
        PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_TOKEN, "");
        PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_USER, null);
        EventBus.getDefault().post(EVENT_LOG_OUT);
        finish();
    }

    @OnClick(R.id.settings_privacy_layout)
    public void clickPrivacy() {
        JumpUtils.jumpToBrowser(PRIVACY_POLICY_URL);
    }
}
