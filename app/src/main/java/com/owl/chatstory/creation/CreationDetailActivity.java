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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.Constants;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.util.TimeUtils;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.OperationRequest;
import com.owl.chatstory.data.eventsource.CreationDetailEvent;
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
    private static final String EXTRA_LANGUAGE = "EXTRA_LANGUAGE";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.directory_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.common_progressbar_layout)
    View mLoadingView;

    private CreationDetailContract.Presenter mPresenter;
    private HeaderAndFooterWrapper<FictionDetailModel> mAdapter;
    private FictionDetailModel mFictionDetailModel;
    private List<FictionModel> mDatas = new ArrayList<>();
    private View mHeaderView;
    private String mFictionId, mLanguage;
    private TextView mChaptersView;

    public static void start(Context context, String id, String language) {
        Intent intent = new Intent(context, CreationDetailActivity.class);
        intent.putExtra(EXTRA_FICTION_ID, id);
        intent.putExtra(EXTRA_LANGUAGE, language);
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
        mLanguage = getIntent().getStringExtra(EXTRA_LANGUAGE);
        CommonAdapter<FictionModel> adapter = new CommonAdapter<FictionModel>(this, R.layout.creation_chapter_item, mDatas) {
            @Override
            protected void convert(ViewHolder holder, final FictionModel fictionModel, int position) {
                holder.setText(R.id.chapter_item_title_txv, getString(R.string.chapter_num, position, fictionModel.getName()));
                holder.setText(R.id.chapter_item_time_txv, TimeUtils.getTimeFormat(fictionModel.getUpdateline()));
                holder.setText(R.id.chapter_item_state_txv, Constants.getStatus(fictionModel.getStatus()));
                holder.setOnClickListener(R.id.chapter_item_more_img, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditDialog(fictionModel);
                    }
                });
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fictionModel.getStatus() == Constants.STATUS_CREATING) {
                            CreateActivity.start(CreationDetailActivity.this, fictionModel);
                        } else {
                            if (mPresenter != null) {
                                mPresenter.getChapterDetail(fictionModel.getId(), mFictionDetailModel.getLanguage());
                                mLoadingView.setVisibility(View.VISIBLE);
                            }
                        }
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
            case R.id.menu_under:
                if (mPresenter != null) {
                    OperationRequest request = new OperationRequest();
                    request.setIfiction_id(mFictionId);
                    request.setLanguage(mLanguage);
                    request.setOp(Constants.OPERATION_DELETE);
                    mPresenter.operateFiction(request);
                }
                break;
            case R.id.menu_recover:
                if (mPresenter != null) {
                    OperationRequest request = new OperationRequest();
                    request.setIfiction_id(mFictionId);
                    request.setLanguage(mLanguage);
                    request.setOp(Constants.OPERATION_RECOVER);
                    mPresenter.operateFiction(request);
                }
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
        mChaptersView = ((TextView) mHeaderView.findViewById(R.id.chapter_header_chapters_txv));
        mChaptersView.setText(getString(R.string.chapter_total_chapters, mDatas.size()));
        mHeaderView.findViewById(R.id.chapter_header_edit_img).setVisibility(View.VISIBLE);
        mHeaderView.findViewById(R.id.chapter_header_edit_img).setOnClickListener(this);
        TextView addChapterView = (TextView) (mHeaderView.findViewById(R.id.chapter_header_add_chapters_txv));
        if (fictionDetailModel.getSerials()) {
            addChapterView.setVisibility(View.VISIBLE);
        }
        addChapterView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chapter_header_edit_img:
                BasicCreateActivity.start(this, mFictionDetailModel);
                break;
            case R.id.chapter_header_add_chapters_txv:
                FictionModel model = new FictionModel();
                model.setIfiction_id(mFictionId);
                model.setLanguage(mLanguage);
                model.setNum(mDatas.size() + 1);
                model.setStatus(Constants.STATUS_CREATING);
                CreateActivity.start(CreationDetailActivity.this, model);
                break;
        }
    }

    @Override
    public void showFictionDetail(FictionDetailModel model) {
        mFictionDetailModel = model;
        setFictionDetail(model);
    }

    @Override
    public void showChapterList(List<FictionModel> list) {
        if (list != null) {
            if (mDatas == null) {
                mDatas = new ArrayList<>();
            }
            mDatas.clear();
            mDatas.addAll(list);
            mAdapter.notifyDataSetChanged();
            if (mChaptersView != null) {
                mChaptersView.setText(getString(R.string.chapter_total_chapters, mDatas.size()));
            }
        } else {
            Toast.makeText(this, "获取章节列表失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showFictionModel(FictionModel model) {
        mLoadingView.setVisibility(View.GONE);
        CreateActivity.start(this, model);
    }

    @Override
    public void operateFictionFinished(boolean success) {
        Toast.makeText(this, "操作小说成功 " + success, Toast.LENGTH_SHORT).show();
        if (mPresenter != null) {
            mPresenter.getFictionDetail(mFictionId, mLanguage);
        }
    }

    @Override
    public void operateChapterFinished(boolean success) {
        Toast.makeText(this, "操作章节成功 " + success, Toast.LENGTH_SHORT).show();
        if (mPresenter != null) {
            mPresenter.getChapterList(mFictionId, mLanguage);
        }
    }

    @Override
    public void publishSuccess() {
        if (mPresenter != null) {
            mPresenter.getChapterList(mFictionId, mLanguage);
        }
    }

    @Override
    public void publishFailed() {
        Toast.makeText(this, "发布失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(CreationDetailContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getFictionDetail(mFictionId, mLanguage);
        mPresenter.getChapterList(mFictionId, mLanguage);
    }

    @Subscribe
    public void updateEvent(FictionDetailModel fictionDetailModel) {
        if (fictionDetailModel != null) {
            mFictionDetailModel = fictionDetailModel;
            setFictionDetail(mFictionDetailModel);
        }
    }

    @Subscribe
    public void updateChapterList(CreationDetailEvent event) {
        if (event != null) {
            if (event.isReload() && mPresenter != null) {
                mPresenter.getChapterList(mFictionId, mLanguage);
            }
        }
    }

    private void showEditDialog(final FictionModel model) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_chapter_edit_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        TextView editView = (TextView) view.findViewById(R.id.dialog_chapter_edit_txv);
        TextView publishView = (TextView) view.findViewById(R.id.dialog_chapter_publish_txv);
        TextView deleteView = (TextView) view.findViewById(R.id.dialog_chapter_delete_txv);
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getStatus() == Constants.STATUS_CREATING) {
                    CreateActivity.start(CreationDetailActivity.this, model);
                } else {
                    if (mPresenter != null) {
                        mPresenter.getChapterDetail(model.getId(), mFictionDetailModel.getLanguage());
                        mLoadingView.setVisibility(View.VISIBLE);
                    }
                }
                alertDialog.dismiss();
            }
        });
        publishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getStatus() == Constants.STATUS_CREATING) {
                    DialogUtils.showDialog(CreationDetailActivity.this, R.string.create_publish_chapter
                            , R.string.create_dialog_cancel, R.string.create_dialog_ok
                            , new DialogUtils.OnDialogClickListener() {
                                @Override
                                public void onOK() {
                                    if (mPresenter != null) {
                                        mPresenter.publishChapter(model);
                                    }
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                } else {
                    if (mPresenter != null) {
                        OperationRequest request = new OperationRequest();
                        request.setIfiction_id(mFictionId);
                        request.setLanguage(mLanguage);
                        request.setChapter_id(model.getId());
                        request.setOp(Constants.OPERATION_RECOVER);
                        mPresenter.operateChapter(request);
                    }
                }
                alertDialog.dismiss();
            }
        });
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getStatus() == Constants.STATUS_CREATING) {
                    PreferencesHelper.getInstance().removeLocalChapter(model);
                    if (mPresenter != null) {
                        mPresenter.getChapterList(mFictionId, mLanguage);
                    }
                } else {
                    if (mPresenter != null) {
                        OperationRequest request = new OperationRequest();
                        request.setIfiction_id(mFictionId);
                        request.setLanguage(mLanguage);
                        request.setChapter_id(model.getId());
                        request.setOp(Constants.OPERATION_DELETE);
                        mPresenter.operateChapter(request);
                    }
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
