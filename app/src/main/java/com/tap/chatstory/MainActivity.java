package com.tap.chatstory;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.tap.chatstory.base.BaseActivity;
import com.tap.chatstory.billing.IabHelper;
import com.tap.chatstory.billing.IabResult;
import com.tap.chatstory.billing.Inventory;
import com.tap.chatstory.billing.Purchase;
import com.tap.chatstory.chat.DirectoryActivity;
import com.tap.chatstory.common.util.Constants;
import com.tap.chatstory.common.util.DialogUtils;
import com.tap.chatstory.common.util.PreferencesHelper;
import com.tap.chatstory.common.view.UnScrollViewPager;
import com.tap.chatstory.creation.MyCreationFragment;
import com.tap.chatstory.data.homesource.model.UpdateModel;
import com.tap.chatstory.home.HomeFragment;
import com.tap.chatstory.user.info.UserFragment;
import com.tap.chatstory.user.message.MessageActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static com.tap.chatstory.common.util.Constants.BASE64_ENCODED_PUBLIC_KEY;
import static com.tap.chatstory.common.util.Constants.ONE_MONTH_SKU;
import static com.tap.chatstory.common.util.Constants.THREE_MONTHS_SKU;
import static com.tap.chatstory.common.util.Constants.WEEK_SKU;
import static com.tap.chatstory.common.util.Constants.YEAR_SKU;

public class MainActivity extends BaseActivity implements MainContract.View {
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
    public static final String EXTRA_FICTION_ID = "EXTRA_FICTION_ID";
    private static final String CHAPTER_FINISHED_ADS_INTERVAL = "chapter_finished_ads_interval";
    private static final int[] HOME_TOOLBAR_TITLE = {
            R.string.main_tab_home,
            R.string.common_create,
            R.string.main_tab_account
    };
    @BindView(R.id.main_viewpager)
    UnScrollViewPager mViewPager;
    @BindView(R.id.main_content_bottombar)
    BottomNavigationView mBottomNavigationView;
    private MainContract.Presenter mPresenter;
    private IabHelper mHelper;
    private Badge mBadge;
    private long mClickTime = 0;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolBar() {
    }

    @Override
    protected void initViewsAndData() {
        MobileAds.initialize(this, "ca-app-pub-8805953710729771~1901274570");
        checkPurchase();
        initFirebaseConfig();
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main_bottom_nav_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.main_bottom_nav_add:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.main_bottom_nav_account:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

        //处理通知过来的逻辑
        int fromNotification = getIntent().getIntExtra(EXTRA_FROM_NOTIFICATION, -1);
        if (fromNotification != -1) {
            if (fromNotification == Constants.MESSAGE_FICTION || fromNotification == Constants.MESSAGE_STAR) {
                String fictionId = getIntent().getStringExtra(EXTRA_FICTION_ID);
                if (fictionId != null) {
                    DirectoryActivity.start(this, fictionId);
                }
            } else {
                MessageActivity.start(this);
            }
        }

        new MainPresenter(this);
    }

    public void setBadge(boolean show) {
        if (mBadge == null) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
            mBadge = new QBadgeView(this).bindTarget(menuView.getChildAt(2)).setGravityOffset(40, 6, true);
        } else {
            if (show) {
                mBadge.setBadgeText("");
            } else {
                mBadge.hide(false);
            }
        }
    }

    private void checkPurchase() {
        try {
            String base64EncodedPublicKey = BASE64_ENCODED_PUBLIC_KEY;
            base64EncodedPublicKey.trim();
            // compute your public key and store it in base64EncodedPublicKey
            mHelper = new IabHelper(this, base64EncodedPublicKey);
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    if (result.isFailure() || mHelper == null) {
                        // Oh no, there was a problem.
                        Log.i("IabHelper", "Problem setting up In-app Billing: " + result);
                        return;
                    }
                    mHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
                        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                            if (result.isFailure()) {
                                Log.i("IabHelper", "onQueryInventoryFinished failed");
                                return;
                            }
                            boolean hasPaid = false;
                            if (inventory != null) {
                                Purchase purchase = inventory.getPurchase(WEEK_SKU);
                                if (purchase != null) {
                                    Log.i("IabHelper", WEEK_SKU + " is available~~~~");
                                    hasPaid = true;
                                    PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_PAID_FOR_VIP, purchase.getSku());
                                    PreferencesHelper.getInstance().setBoolean(PreferencesHelper.KEY_NO_ADS_VIP, false);
                                } else {
                                    Log.i("IabHelper", WEEK_SKU + " is not available~~~~");
                                }

                                Purchase purchase1 = inventory.getPurchase(ONE_MONTH_SKU);
                                if (purchase1 != null) {
                                    Log.i("IabHelper", ONE_MONTH_SKU + " is available~~~~");
                                    hasPaid = true;
                                    PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_PAID_FOR_VIP, purchase1.getSku());
                                    PreferencesHelper.getInstance().setBoolean(PreferencesHelper.KEY_NO_ADS_VIP, true);
                                } else {
                                    Log.i("IabHelper", ONE_MONTH_SKU + " is not available~~~~");
                                }

                                Purchase purchase2 = inventory.getPurchase(THREE_MONTHS_SKU);
                                if (purchase2 != null) {
                                    Log.i("IabHelper", THREE_MONTHS_SKU + " is available~~~~");
                                    hasPaid = true;
                                    PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_PAID_FOR_VIP, purchase2.getSku());
                                    PreferencesHelper.getInstance().setBoolean(PreferencesHelper.KEY_NO_ADS_VIP, true);
                                } else {
                                    Log.i("IabHelper", THREE_MONTHS_SKU + " is not available~~~~");
                                }

                                Purchase purchase3 = inventory.getPurchase(YEAR_SKU);
                                if (purchase3 != null) {
                                    Log.i("IabHelper", YEAR_SKU + " is available~~~~");
                                    hasPaid = true;
                                    PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_PAID_FOR_VIP, purchase3.getSku());
                                    PreferencesHelper.getInstance().setBoolean(PreferencesHelper.KEY_NO_ADS_VIP, true);
                                } else {
                                    Log.i("IabHelper", YEAR_SKU + " is not available~~~~");
                                }
                            }
                            //没查询到任何一个购买则认为没有购买会员或者会员已经过期
                            if (!hasPaid) {
                                PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_PAID_FOR_VIP, null);
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (Exception e) {
            }
            mHelper = null;
        }
    }

    @Override
    public void showUpdateDialog(UpdateModel model) {
        DialogUtils.showUpdateDialog(this, model, null);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.subscribe();
    }

    private void initFirebaseConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        long cacheExpiration = 60 * 60 * 12;
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                            Log.i("Lebron", " remote config " + mFirebaseRemoteConfig.getLong(CHAPTER_FINISHED_ADS_INTERVAL));
                        }
                        PreferencesHelper.getInstance().setInt(PreferencesHelper.KEY_CHAPTER_ADS_INTERVAL, (int) mFirebaseRemoteConfig.getLong(CHAPTER_FINISHED_ADS_INTERVAL));
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mClickTime) > 3000) {
            Toast.makeText(getApplicationContext(), R.string.main_exit_confirm, Toast.LENGTH_SHORT).show();
            mClickTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {
        private Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (HOME_TOOLBAR_TITLE[position]) {
                case R.string.main_tab_home:
                    fragment = new HomeFragment();
                    break;
                case R.string.common_create:
                    fragment = new MyCreationFragment();
                    break;
                case R.string.main_tab_account:
                    fragment = UserFragment.newInstance();
                    break;
            }
            mPageReferenceMap.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        @Override
        public int getCount() {
            return HOME_TOOLBAR_TITLE.length;
        }

        public Fragment getFragment(int key) {
            return mPageReferenceMap.get(key);
        }
    }
}
