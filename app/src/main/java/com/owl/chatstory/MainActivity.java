package com.owl.chatstory;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.billing.IabHelper;
import com.owl.chatstory.billing.IabResult;
import com.owl.chatstory.billing.Inventory;
import com.owl.chatstory.billing.Purchase;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.view.UnScrollViewPager;
import com.owl.chatstory.creation.MyCreationFragment;
import com.owl.chatstory.data.homesource.model.UpdateModel;
import com.owl.chatstory.home.HomeFragment;
import com.owl.chatstory.user.info.UserFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.owl.chatstory.common.util.Constants.BASE64_ENCODED_PUBLIC_KEY;
import static com.owl.chatstory.common.util.Constants.ONE_MONTH_SKU;
import static com.owl.chatstory.common.util.Constants.THREE_MONTHS_SKU;
import static com.owl.chatstory.common.util.Constants.WEEK_SKU;
import static com.owl.chatstory.common.util.Constants.YEAR_SKU;

public class MainActivity extends BaseActivity implements MainContract.View {
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

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolBar() {
        Log.i("Lebron", "the key is " + getIntent().getStringExtra("key"));
    }

    @Override
    protected void initViewsAndData() {
        checkPurchase();
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
        new MainPresenter(this);
    }

    private void checkPurchase() {
        try {
            String base64EncodedPublicKey = BASE64_ENCODED_PUBLIC_KEY;
            base64EncodedPublicKey.trim();
            // compute your public key and store it in base64EncodedPublicKey
            mHelper = new IabHelper(this, base64EncodedPublicKey);
            mHelper.enableDebugLogging(false);
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
                                } else {
                                    Log.i("IabHelper", WEEK_SKU + " is not available~~~~");
                                }

                                Purchase purchase1 = inventory.getPurchase(ONE_MONTH_SKU);
                                if (purchase1 != null) {
                                    Log.i("IabHelper", ONE_MONTH_SKU + " is available~~~~");
                                    hasPaid = true;
                                    PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_PAID_FOR_VIP, purchase1.getSku());
                                } else {
                                    Log.i("IabHelper", ONE_MONTH_SKU + " is not available~~~~");
                                }

                                Purchase purchase2 = inventory.getPurchase(THREE_MONTHS_SKU);
                                if (purchase2 != null) {
                                    Log.i("IabHelper", THREE_MONTHS_SKU + " is available~~~~");
                                    hasPaid = true;
                                    PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_PAID_FOR_VIP, purchase2.getSku());
                                } else {
                                    Log.i("IabHelper", THREE_MONTHS_SKU + " is not available~~~~");
                                }

                                Purchase purchase3 = inventory.getPurchase(YEAR_SKU);
                                if (purchase3 != null) {
                                    Log.i("IabHelper", YEAR_SKU + " is available~~~~");
                                    hasPaid = true;
                                    PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_PAID_FOR_VIP, purchase3.getSku());
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
            mHelper.dispose();
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
