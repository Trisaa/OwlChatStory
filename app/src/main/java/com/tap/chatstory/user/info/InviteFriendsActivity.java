package com.tap.chatstory.user.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.tap.chatstory.R;
import com.tap.chatstory.base.BaseActivity;
import com.tap.chatstory.common.util.DialogUtils;
import com.tap.chatstory.common.util.ImageLoaderUtils;
import com.tap.chatstory.common.util.ShareUtils;
import com.tap.chatstory.data.homesource.model.ShareModel;
import com.tap.chatstory.data.usersource.model.UserModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 2018/4/4.
 */

public class InviteFriendsActivity extends BaseActivity {
    public static final String EXTRA_USERMODEL = "EXTRA_USERMODEL";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.read_right_name)
    TextView mNameView;
    @BindView(R.id.read_right_name1)
    TextView mNameView1;
    @BindView(R.id.read_right_icon)
    ImageView mIcon;
    @BindView(R.id.read_right_icon1)
    ImageView mIcon1;
    @BindView(R.id.invite_code_txv)
    TextView mInviteCode;

    public static void start(Context context, UserModel userModel) {
        Intent intent = new Intent(context, InviteFriendsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_USERMODEL, userModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_invite_friends;
    }

    @Override
    protected void initToolBar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    protected void initViewsAndData() {
        UserModel userModel = getIntent().getParcelableExtra(EXTRA_USERMODEL);
        if (userModel != null) {
            mInviteCode.setText(userModel.getInviteCode());
            mNameView.setText(userModel.getName());
            mNameView1.setText(userModel.getName());
            ImageLoaderUtils.getInstance().loadCircleImage(this, userModel.getIcon(), mIcon);
            ImageLoaderUtils.getInstance().loadCircleImage(this, userModel.getIcon(), mIcon1);
        }
    }

    @OnClick(R.id.invite_now_txv)
    public void clickInvite() {
        ShareModel shareModel = new ShareModel();
        shareModel.setContent(getString(R.string.share_content));
        shareModel.setUrl(ShareUtils.getShareAppUrl(this));
        DialogUtils.showShareDialog(this, shareModel);
    }
}
