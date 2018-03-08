package com.owl.chatstory.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.chat.DirectoryActivity;
import com.owl.chatstory.chat.FavoriteActivity;
import com.owl.chatstory.chat.ReadActivity;
import com.owl.chatstory.common.util.CommonVerticalItemDecoration;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.util.KeyboardUtils;
import com.owl.chatstory.data.searchsource.SearchDetailModel;
import com.owl.chatstory.data.searchsource.SearchModel;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 2018/1/18.
 */

public class SearchActivity extends BaseActivity implements SearchContract.View, TextView.OnEditorActionListener {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    View mEmptyView;
    @BindView(R.id.common_progressbar_layout)
    View mLoadingView;
    @BindView(R.id.search_edt)
    EditText mEditText;

    private SearchContract.Presenter mPresenter;
    private CommonAdapter<SearchDetailModel> mAdapter;
    private List<SearchDetailModel> mDatas = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_search;
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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @OnClick(R.id.search_edittext_close)
    public void clickClear() {
        mEditText.setText("");
    }

    @Override
    protected void initViewsAndData() {
        mEditText.setOnEditorActionListener(this);
        mAdapter = new CommonAdapter<SearchDetailModel>(this, R.layout.story_category_item, mDatas) {

            @Override
            protected void convert(ViewHolder holder, SearchDetailModel searchDetailModel, int position) {
                ImageLoaderUtils.getInstance().loadImage(SearchActivity.this, searchDetailModel.getCover(), (ImageView) holder.getView(R.id.category_item_cover_img), R.color.colorPrimaryDark);
                holder.setText(R.id.category_item_title_txv, searchDetailModel.getTitle());
                holder.setText(R.id.category_item_description_txv, searchDetailModel.getSummary());
                holder.setText(R.id.category_item_watchers_txv, searchDetailModel.getViews() + "");
                holder.setVisible(R.id.category_upinfo_txv, false);
                holder.setVisible(R.id.category_item_vip_img, searchDetailModel.getVip() == 0 ? false : true);
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                DirectoryActivity.start(SearchActivity.this, mDatas.get(position).getFictionId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new CommonVerticalItemDecoration(32));
        mRecyclerView.setAdapter(mAdapter);
        new SearchPresenter(this);
    }

    @Override
    public void showSearchData(SearchModel model) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(model.getList());
        mAdapter.notifyDataSetChanged();
        mLoadingView.setVisibility(View.GONE);
        if (model.getList().size() > 0) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            if (!TextUtils.isEmpty(mEditText.getText().toString())) {
                mPresenter.searchData(mEditText.getText().toString());
                mLoadingView.setVisibility(View.VISIBLE);
                KeyboardUtils.hideKeyboard(mEditText);
            }
            return true;
        }
        return false;
    }
}
