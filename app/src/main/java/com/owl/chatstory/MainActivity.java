package com.owl.chatstory;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.view.UnScrollViewPager;
import com.owl.chatstory.creation.MyCreationFragment;
import com.owl.chatstory.data.homesource.model.UpdateModel;
import com.owl.chatstory.home.HomeFragment;
import com.owl.chatstory.user.info.UserFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View {
    @BindView(R.id.main_viewpager)
    UnScrollViewPager mViewPager;
    @BindView(R.id.main_content_bottombar)
    BottomNavigationView mBottomNavigationView;

    private MainContract.Presenter mPresenter;

    private static final int[] HOME_TOOLBAR_TITLE = {
            R.string.main_tab_home,
            R.string.common_create,
            R.string.main_tab_account
    };

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initViewsAndData() {
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
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        new MainPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
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
