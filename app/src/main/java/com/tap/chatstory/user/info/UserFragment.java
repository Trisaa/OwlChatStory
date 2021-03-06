package com.tap.chatstory.user.info;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.tap.chatstory.MainActivity;
import com.tap.chatstory.R;
import com.tap.chatstory.base.BaseFragment;
import com.tap.chatstory.chat.FavoriteActivity;
import com.tap.chatstory.chat.HistoryActivity;
import com.tap.chatstory.common.util.DialogUtils;
import com.tap.chatstory.common.util.ImageLoaderUtils;
import com.tap.chatstory.common.util.JumpUtils;
import com.tap.chatstory.common.util.PreferencesHelper;
import com.tap.chatstory.data.usersource.model.UserModel;
import com.tap.chatstory.settings.SettingsActivity;
import com.tap.chatstory.user.login.LoginActivity;
import com.tap.chatstory.user.message.MessageActivity;
import com.tap.chatstory.user.page.UserPageActivity;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 2017/9/11.
 */

public class UserFragment extends BaseFragment implements UserContract.View {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_img)
    ImageView mUserIcon;
    @BindView(R.id.user_name_txv)
    TextView mNameView;
    @BindView(R.id.user_vip_sign)
    ImageView mVipView;
    @BindView(R.id.user_settings_message_count_txv)
    TextView mCountView;

    private UserModel mUserModel;
    private UserContract.Presenter mPresenter;

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_user;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferencesHelper.getInstance().isLogined() && mPresenter != null) {
            mPresenter.getUnreadCount();
        }

    }

    @Override
    protected void initViewsAndData(View view) {
        initToolbar();
        mUserModel = new Gson().fromJson(PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_USER, null), UserModel.class);
        updateUserInfo();
        updateVipInfo();
        new UserPresenter(this);
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

    private void updateVipInfo() {
        String sku = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_PAID_FOR_VIP, null);
        if (!TextUtils.isEmpty(sku)) {
            mVipView.setVisibility(View.VISIBLE);
        } else {
            mVipView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.toolbar_edit)
    public void clickEdit() {
        if (PreferencesHelper.getInstance().isLogined()) {
            EditUserActivity.start(getActivity(), mUserModel);
        } else {
            Toast.makeText(getActivity(), R.string.common_login_first, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.user_settings_vip_layout)
    public void clickVip() {
        VIPActivity.start(getActivity());
    }

    @OnClick(R.id.user_settings_userpage_layout)
    public void clickUserPage() {
        if (mUserModel != null) {
            Intent intent = new Intent(getActivity(), UserPageActivity.class);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), mUserIcon, getString(R.string.share_user_icon));
            startActivity(intent, transitionActivityOptions.toBundle());
        }
    }

    @OnClick(R.id.user_settings_message_layout)
    public void clickMessage() {
        if (PreferencesHelper.getInstance().isLogined()) {
            MessageActivity.start(getActivity());
        } else {
            Toast.makeText(getActivity(), R.string.common_login_first, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.user_settings_history_layout)
    public void clickHistory() {
        if (PreferencesHelper.getInstance().isLogined()) {
            HistoryActivity.start(getActivity());
        } else {
            Toast.makeText(getActivity(), R.string.common_login_first, Toast.LENGTH_SHORT).show();
        }
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

    @OnClick(R.id.user_settings_favorite_layout)
    public void clickFavorite() {
        if (PreferencesHelper.getInstance().isLogined()) {
            FavoriteActivity.start(getActivity());
        } else {
            Toast.makeText(getActivity(), R.string.common_login_first, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.user_settings_hire_layout)
    public void clickWiter() {
        JumpUtils.jumpToBrowser("https://d.eqxiu.com/s/G5q5drtz");
    }

    @OnClick(R.id.user_settings_wallet_layout)
    public void clickWallet() {
        if (PreferencesHelper.getInstance().isLogined()) {
            WalletActivity.start(getActivity());
        } else {
            Toast.makeText(getActivity(), R.string.common_login_first, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.user_settings_tasks_layout)
    public void clickDailyTask() {
        if (PreferencesHelper.getInstance().isLogined()) {
            DailyTaskActivity.start(getActivity());
        } else {
            Toast.makeText(getActivity(), R.string.common_login_first, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.user_settings_invite_layout)
    public void clickInvite() {
        if (mUserModel != null) {
            InviteFriendsActivity.start(getActivity(), mUserModel);
        } else {
            Toast.makeText(getActivity(), R.string.common_login_first, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.user_settings_invite_code_layout)
    public void inputInviteCode() {
        if (mUserModel != null) {
            DialogUtils.showInputInviteCodeDialog(getActivity(), mUserModel.getHasInvited(), new DialogUtils.OnInviteCodeListener() {
                @Override
                public void onCode(String code) {
                    mPresenter.inputInviteCode(code);
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.common_login_first, Toast.LENGTH_SHORT).show();
        }
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

    @Subscribe
    public void paidEvent(String event) {
        if (VIPActivity.EVENT_PAID_FOR_VIP.equals(event)) {
            updateVipInfo();
        }
    }

    @Override
    public void showUserInfo(UserModel userModel) {
        if (userModel != null) {
            mUserModel = userModel;
            updateUserInfo();
            if (TextUtils.isEmpty(mUserModel.getDeviceToken())) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.i("Lebron", "device token " + refreshedToken);
                mPresenter.uploadDeviceToken(refreshedToken);
            }
        } else {
            Toast.makeText(getActivity(), R.string.common_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessageCount(int count) {
        mCountView.setText(count > 99 ? "..." : String.valueOf(count));
        ((MainActivity) getActivity()).setBadge(count > 0);
    }

    @Override
    public void showRewarded() {
        DialogUtils.showGetCoinsDialog(getActivity(), getString(R.string.coin_getted));
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        mPresenter = presenter;
        if (PreferencesHelper.getInstance().isLogined()) {
            mPresenter.subscribe();
        }
    }
}
