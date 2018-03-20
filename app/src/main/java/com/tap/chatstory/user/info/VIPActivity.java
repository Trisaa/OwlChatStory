package com.tap.chatstory.user.info;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.tap.chatstory.R;
import com.tap.chatstory.base.BaseActivity;
import com.tap.chatstory.billing.IabHelper;
import com.tap.chatstory.billing.IabResult;
import com.tap.chatstory.billing.Purchase;
import com.tap.chatstory.common.util.FeedbackUtils;
import com.tap.chatstory.common.util.JumpUtils;
import com.tap.chatstory.common.util.PreferencesHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tap.chatstory.common.util.Constants.BASE64_ENCODED_PUBLIC_KEY;
import static com.tap.chatstory.common.util.Constants.ONE_MONTH_SKU;
import static com.tap.chatstory.common.util.Constants.THREE_MONTHS_SKU;
import static com.tap.chatstory.common.util.Constants.WEEK_SKU;
import static com.tap.chatstory.common.util.Constants.YEAR_SKU;
import static com.tap.chatstory.common.util.JumpUtils.FACEBOOK_PAGE_URL;

/**
 * Created by lebron on 2018/1/30.
 */

public class VIPActivity extends BaseActivity {
    public static final String EVENT_PAID_FOR_VIP = "EVENT_PAID_FOR_VIP";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    private IabHelper mHelper;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (mHelper == null || result.isFailure()) {
                Log.i("Lebron", " Purchase failed " + result.getMessage());
                return;
            }
            Log.i("Lebron", "Purchase successful." + purchase.getSku());
            PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_PAID_FOR_VIP, purchase.getSku());
            if (WEEK_SKU.equals(purchase.getSku())) {
                PreferencesHelper.getInstance().setBoolean(PreferencesHelper.KEY_NO_ADS_VIP, false);
            } else {
                PreferencesHelper.getInstance().setBoolean(PreferencesHelper.KEY_NO_ADS_VIP, true);
            }
            EventBus.getDefault().post(EVENT_PAID_FOR_VIP);
            finish();
        }
    };
    private boolean isIabHelperReady;

    public static void start(Context context) {
        Intent intent = new Intent(context, VIPActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_vip;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mHelper == null) {
            return;
        }
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void initViewsAndData() {
        try {
            String base64EncodedPublicKey = BASE64_ENCODED_PUBLIC_KEY;
            base64EncodedPublicKey.trim();
            // compute your public key and store it in base64EncodedPublicKey
            mHelper = new IabHelper(this, base64EncodedPublicKey);
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    if (result.isFailure()) {
                        // Oh no, there was a problem.
                        Log.i("Lebron", "Problem setting up In-app Billing: " + result);
                        isIabHelperReady = false;
                        return;
                    } else {
                        isIabHelperReady = true;
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    @OnClick(R.id.week_subscribe_btn)
    public void clickPurchaseByWeek() {
        if (mHelper != null && isIabHelperReady) {
            mHelper.launchSubscriptionPurchaseFlow(this, WEEK_SKU, 1000, mPurchaseFinishedListener);
        }
    }

    @OnClick(R.id.one_month_subscribe_btn)
    public void clickPurchaseByMonth() {
        if (mHelper != null && isIabHelperReady) {
            mHelper.launchSubscriptionPurchaseFlow(this, ONE_MONTH_SKU, 1000, mPurchaseFinishedListener);
        }
    }

    @OnClick(R.id.three_months_subscribe_btn)
    public void clickPurchaseByMonths() {
        if (mHelper != null && isIabHelperReady) {
            mHelper.launchSubscriptionPurchaseFlow(this, THREE_MONTHS_SKU, 1000, mPurchaseFinishedListener);
        }
    }

    @OnClick(R.id.year_subscribe_btn)
    public void clickPurchaseByYear() {
        if (mHelper != null && isIabHelperReady) {
            mHelper.launchSubscriptionPurchaseFlow(this, YEAR_SKU, 1000, mPurchaseFinishedListener);
        }
    }

    @OnClick(R.id.facebook_page_txv)
    public void clickFacebookPage() {
        JumpUtils.jumpToBrowser(FACEBOOK_PAGE_URL);
    }

    @OnClick(R.id.login_privacy_txv)
    public void clickGmail() {
        FeedbackUtils.startFeedbackActivity(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (Exception e) {
            }
            mHelper = null;
        }
    }
}
