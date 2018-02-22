package com.owl.chatstory.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.util.ShareUtils;
import com.owl.chatstory.common.util.TimeUtils;
import com.owl.chatstory.data.chatsource.model.ChapterModel;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.homesource.model.ShareModel;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lebron on 2017/10/30.
 */

public class DirectoryActivity extends BaseActivity {
    private static final String EXTRA_FICTION_MODEL = "EXTRA_FICTION_MODEL";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.directory_recycler)
    RecyclerView mRecyclerView;

    private HeaderAndFooterWrapper<FictionDetailModel> mAdapter;
    private List<ChapterModel> mDatas = new ArrayList<>();

    public static void start(Context context, FictionDetailModel model) {
        Intent intent = new Intent(context, DirectoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FICTION_MODEL, model);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_directory;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                ShareModel shareModel = new ShareModel();
                shareModel.setContent(getString(R.string.share_content));
                shareModel.setUrl(ShareUtils.getShareAppUrl(this));
                DialogUtils.showShareDialog(this, shareModel);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewsAndData() {
        FictionDetailModel fictionDetailModel = getIntent().getParcelableExtra(EXTRA_FICTION_MODEL);
        final List<String> list = PreferencesHelper.getInstance().getFictionProgressList(fictionDetailModel.getId(), fictionDetailModel.getChapters().size());
        if (fictionDetailModel != null) {
            mDatas = fictionDetailModel.getChapters();
            CommonAdapter<ChapterModel> adapter = new CommonAdapter<ChapterModel>(this, R.layout.directory_chapter_item, mDatas) {
                @Override
                protected void convert(ViewHolder holder, final ChapterModel chapterModel, int position) {
                    try {
                        holder.setText(R.id.chapter_item_title_txv, getString(R.string.chapter_num, position, chapterModel.getChapterName()));
                        holder.setText(R.id.chapter_item_time_txv, TimeUtils.getTimeFormat(chapterModel.getCreateTime()));
                        holder.setVisible(R.id.chapter_item_vip_img, chapterModel.getVip() == 0 ? false : true);
                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EventBus.getDefault().post(chapterModel);
                                finish();
                            }
                        });
                        if (position - 1 < list.size()) {
                            holder.setText(R.id.chapter_item_progress_txv, list.get(position - 1) + "%");
                        } else {
                            holder.setText(R.id.chapter_item_progress_txv, 0 + "%");
                        }
                    } catch (Exception e) {
                    }
                }
            };
            mAdapter = new HeaderAndFooterWrapper<>(adapter);
            View view = getLayoutInflater().inflate(R.layout.directory_chapter_headerview, null);
            ImageLoaderUtils.getInstance().loadImage(this, fictionDetailModel.getCover(), (ImageView) view.findViewById(R.id.chapter_header_cover_img), R.color.colorPrimaryDark);
            ((TextView) view.findViewById(R.id.chapter_header_title_txv)).setText(fictionDetailModel.getTitle());
            String author = TextUtils.isEmpty(fictionDetailModel.getWriter().getName()) ? getString(R.string.app_name) : fictionDetailModel.getWriter().getName();
            ((TextView) view.findViewById(R.id.chapter_header_author_txv)).setText(author);
            ((TextView) view.findViewById(R.id.chapter_header_describe_txv)).setText(fictionDetailModel.getSummary());
            ((TextView) view.findViewById(R.id.chapter_header_tags_txv)).setText(fictionDetailModel.getTags().get(0));
            ((TextView) view.findViewById(R.id.chapter_header_chapters_txv)).setText(getString(R.string.chapter_total_chapters, fictionDetailModel.getChapters().size()));
            mAdapter.addHeaderView(view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
