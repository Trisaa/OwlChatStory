package com.owl.chatstory.user.info;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseFragment;
import com.owl.chatstory.chat.HistoryActivity;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.data.usersource.model.UserModel;
import com.owl.chatstory.settings.SettingsActivity;
import com.owl.chatstory.user.login.LoginActivity;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 2017/9/11.
 */

public class UserFragment extends BaseFragment {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_img)
    ImageView mUserIcon;
    @BindView(R.id.user_name_txv)
    TextView mNameView;

    private UserModel mUserModel;

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initViewsAndData(View view) {
        initToolbar();
        mUserModel = new Gson().fromJson(PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_USER, null), UserModel.class);
        updateUserInfo();
    }

    private void updateUserInfo() {
        if (mUserModel != null) {
            ImageLoaderUtils.getInstance().loadCircleImage(getActivity(), mUserModel.getIcon(), mUserIcon);
            mNameView.setText(mUserModel.getName());
        } else {
            ImageLoaderUtils.getInstance().loadCircleImage(getActivity(), R.mipmap.user_default_icon, mUserIcon);
            mNameView.setText(R.string.login_needed);
        }
    }

    private void initToolbar() {
        if (mToolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(false);
                ab.setDisplayShowTitleEnabled(false);
            }
        }
    }

    @OnClick(R.id.user_settings_vip_layout)
    public void clickVip() {

    }

    @OnClick(R.id.user_settings_history_layout)
    public void clickHistory() {
        HistoryActivity.start(getActivity());
    }

    @OnClick({R.id.user_img, R.id.user_name_txv})
    public void clickUserIcon() {
        if (!PreferencesHelper.getInstance().isLogined()) {
            LoginActivity.start(getActivity());
        }
    }

    @OnClick(R.id.user_settings_settings_layout)
    public void clickSettings() {
        SettingsActivity.start(getActivity());
    }

    @Subscribe
    public void loginEvent(UserModel event) {
        mUserModel = event;
        updateUserInfo();
    }

    @Subscribe
    public void logoutEvent(String event) {
        if (SettingsActivity.EVENT_LOG_OUT.equals(event)) {
            mUserModel = null;
            updateUserInfo();
        }
    }
}
