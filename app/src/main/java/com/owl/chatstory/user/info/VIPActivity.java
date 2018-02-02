package com.owl.chatstory.user.info;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.billing.IabHelper;
import com.owl.chatstory.billing.IabResult;
import com.owl.chatstory.billing.Purchase;
import com.owl.chatstory.common.util.PreferencesHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.owl.chatstory.common.util.Constants.BASE64_ENCODED_PUBLIC_KEY;
import static com.owl.chatstory.common.util.Constants.ONE_MONTH_SKU;
import static com.owl.chatstory.common.util.Constants.THREE_MONTHS_SKU;
import static com.owl.chatstory.common.util.Constants.WEEK_SKU;
import static com.owl.chatstory.common.util.Constants.YEAR_SKU;

/**
 * Created by lebron on 2018/1/30.
 */

public class VIPActivity extends BaseActivity {
    public static final String EVENT_PAID_FOR_VIP = "EVENT_PAID_FOR_VIP";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    private IabHelper mHelper;

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
        String base64EncodedPublicKey = BASE64_ENCODED_PUBLIC_KEY;
        base64EncodedPublicKey.trim();
        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isFailure()) {
                    // Oh no, there was a problem.
                    Log.i("Lebron", "Problem setting up In-app Billing: " + result);
                    return;
                }
            }
        });

    }

    @OnClick(R.id.week_subscribe_btn)
    public void clickPurchaseByWeek() {
        mHelper.launchSubscriptionPurchaseFlow(this, WEEK_SKU, 1000, mPurchaseFinishedListener);
    }

    @OnClick(R.id.one_month_subscribe_btn)
    public void clickPurchaseByMonth() {
        mHelper.launchSubscriptionPurchaseFlow(this, ONE_MONTH_SKU, 1000, mPurchaseFinishedListener);
    }

    @OnClick(R.id.three_months_subscribe_btn)
    public void clickPurchaseByMonths() {
        mHelper.launchSubscriptionPurchaseFlow(this, THREE_MONTHS_SKU, 1000, mPurchaseFinishedListener);
    }

    @OnClick(R.id.year_subscribe_btn)
    public void clickPurchaseByYear() {
        mHelper.launchSubscriptionPurchaseFlow(this, YEAR_SKU, 1000, mPurchaseFinishedListener);
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (mHelper == null || result.isFailure()) {
                Log.i("Lebron", " Purchase failed " + result.getMessage());
                return;
            }
            Log.i("Lebron", "Purchase successful."+purchase.getSku());
            PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_PAID_FOR_VIP, purchase.getSku());
            EventBus.getDefault().post(EVENT_PAID_FOR_VIP);
            finish();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }
}
