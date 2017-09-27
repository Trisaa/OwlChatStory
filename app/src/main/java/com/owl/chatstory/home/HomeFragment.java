package com.owl.chatstory.home;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseFragment;
import com.owl.chatstory.data.homesource.model.CategoryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by lebron on 2017/9/10.
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {
    @BindView(R.id.home_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.home_viewpager)
    ViewPager mViewPager;

    private HomePagerAdapter mHomePagerAdapter;
    private HomeContract.Presenter mPresenter;

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViewsAndData(View view) {
        //mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);
        new HomePresenter(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void showCategoryList(List<CategoryModel> list) {
        mHomePagerAdapter = new HomePagerAdapter(getActivity().getSupportFragmentManager(), list);
        mViewPager.setAdapter(mHomePagerAdapter);
        for (int i = 0; i < list.size(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setText(list.get(i).getTitle());
        }
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.subscribe();
    }

    private class HomePagerAdapter extends FragmentStatePagerAdapter {
        private Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();
        private List<CategoryModel> mDatas = new ArrayList<>();

        public HomePagerAdapter(FragmentManager fm, List<CategoryModel> list) {
            super(fm);
            mDatas = list;
        }

        @Override
        public Fragment getItem(int position) {
            CategoryFragment fragment = CategoryFragment.newInstance(mDatas.get(position).getId());
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
            return mDatas.size();
        }

        public Fragment getFragment(int key) {
            if (mPageReferenceMap != null) {
                return mPageReferenceMap.get(key);
            }
            return null;
        }
    }
}
