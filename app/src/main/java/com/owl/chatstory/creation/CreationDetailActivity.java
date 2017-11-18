package com.owl.chatstory.creation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.util.TimeUtils;
import com.owl.chatstory.data.chatsource.model.ChapterModel;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lebron on 2017/11/16.
 */

public class CreationDetailActivity extends BaseActivity implements View.OnClickListener, CreationDetailContract.View {
    private static final String EXTRA_FICTION_ID = "EXTRA_FICTION_ID";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.directory_recycler)
    RecyclerView mRecyclerView;

    private CreationDetailContract.Presenter mPresenter;
    private HeaderAndFooterWrapper<FictionDetailModel> mAdapter;
    private FictionDetailModel mFictionDetailModel;
    private List<ChapterModel> mDatas = new ArrayList<>();
    private View mHeaderView;
    private String mFictionId;

    public static void start(Context context, String id) {
        Intent intent = new Intent(context, CreationDetailActivity.class);
        intent.putExtra(EXTRA_FICTION_ID, id);
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
    protected void initViewsAndData() {
        mFictionId = getIntent().getStringExtra(EXTRA_FICTION_ID);
        CommonAdapter<ChapterModel> adapter = new CommonAdapter<ChapterModel>(this, R.layout.creation_chapter_item, mDatas) {
            @Override
            protected void convert(ViewHolder holder, final ChapterModel chapterModel, int position) {
                holder.setText(R.id.chapter_item_title_txv, getString(R.string.chapter_num, position, chapterModel.getChapterName()));
                holder.setText(R.id.chapter_item_time_txv, TimeUtils.getTimeFormat(chapterModel.getCreateTime()));
                holder.setText(R.id.chapter_item_state_txv, "Creating");
                holder.setOnClickListener(R.id.chapter_item_more_img, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditDialog();
                    }
                });
                holder.setOnClickListener(R.id.chapter_item_title_txv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddChapterDialog(chapterModel.getChapterName());
                    }
                });
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateActivity.start(CreationDetailActivity.this, mFictionId);
                    }
                });
            }
        };
        mAdapter = new HeaderAndFooterWrapper<>(adapter);
        mHeaderView = getLayoutInflater().inflate(R.layout.directory_chapter_headerview, null);
        mAdapter.addHeaderView(mHeaderView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        new CreationDetailPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.creation_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_finished:

                break;
            case R.id.menu_under:
                /*DialogUtils.showDialog(this, R.string.create_publish_chapter
                        , R.string.create_dialog_cancel, R.string.create_dialog_ok
                        , new DialogUtils.OnDialogClickListener() {
                            @Override
                            public void onOK() {
                                finish();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });*/
                break;
            case R.id.menu_delete:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFictionDetail(FictionDetailModel fictionDetailModel) {
        ImageLoaderUtils.getInstance().loadImage(this, fictionDetailModel.getCover(), (ImageView) mHeaderView.findViewById(R.id.chapter_header_cover_img), R.color.colorPrimaryDark);
        ((TextView) mHeaderView.findViewById(R.id.chapter_header_title_txv)).setText(fictionDetailModel.getTitle());
        String author = TextUtils.isEmpty(fictionDetailModel.getWriter().getName()) ? getString(R.string.app_name) : fictionDetailModel.getWriter().getName();
        ((TextView) mHeaderView.findViewById(R.id.chapter_header_author_txv)).setText(author);
        ((TextView) mHeaderView.findViewById(R.id.chapter_header_describe_txv)).setText(fictionDetailModel.getSummary());
        ((TextView) mHeaderView.findViewById(R.id.chapter_header_tags_txv)).setText(fictionDetailModel.getTags().get(0));
        ((TextView) mHeaderView.findViewById(R.id.chapter_header_chapters_txv)).setText(getString(R.string.chapter_total_chapters, fictionDetailModel.getChapters().size()));
        mHeaderView.findViewById(R.id.chapter_header_edit_img).setVisibility(View.VISIBLE);
        TextView addChapterView = (TextView) (mHeaderView.findViewById(R.id.chapter_header_add_chapters_txv));
        addChapterView.setVisibility(View.VISIBLE);
        addChapterView.setOnClickListener(this);
        mHeaderView.findViewById(R.id.chapter_header_edit_img).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chapter_header_edit_img:
                BasicCreateActivity.start(this, mFictionDetailModel);
                break;
            case R.id.chapter_header_add_chapters_txv:
                showAddChapterDialog("");
                break;
        }
    }

    @Override
    public void showFictionDetail(FictionDetailModel model) {
        mFictionDetailModel = model;
        mDatas.addAll(mFictionDetailModel.getChapters());
        setFictionDetail(model);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(CreationDetailContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getFictionDetail(mFictionId);
    }

    @Subscribe
    public void updateEvent(FictionDetailModel fictionDetailModel) {
        if (fictionDetailModel != null) {
            mFictionDetailModel = fictionDetailModel;
            setFictionDetail(mFictionDetailModel);
        }
    }

    private void showAddChapterDialog(String title) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_chapter, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        final EditText editText = (EditText) view.findViewById(R.id.add_chapter_edit);
        if (!TextUtils.isEmpty(title)) {
            editText.setText(title);
            editText.setSelection(title.length());
        }
        view.findViewById(R.id.add_chapter_delete_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.add_chapter_submit_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(title)) {

                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showEditDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_chapter_edit_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.dialog_chapter_edit_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.dialog_chapter_publish_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.dialog_chapter_delete_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
