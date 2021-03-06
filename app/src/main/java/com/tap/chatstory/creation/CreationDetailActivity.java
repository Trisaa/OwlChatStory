package com.tap.chatstory.creation;

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

import com.tap.chatstory.R;
import com.tap.chatstory.base.BaseActivity;
import com.tap.chatstory.common.util.Constants;
import com.tap.chatstory.common.util.DialogUtils;
import com.tap.chatstory.common.util.ImageLoaderUtils;
import com.tap.chatstory.common.util.PreferencesHelper;
import com.tap.chatstory.common.util.TimeUtils;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;
import com.tap.chatstory.data.chatsource.model.FictionModel;
import com.tap.chatstory.data.chatsource.model.OperationRequest;
import com.tap.chatstory.data.eventsource.CreationDetailEvent;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;
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
    @BindView(R.id.directory_fiction_cover)
    ImageView mCoverImage;
    @BindView(R.id.chapter_header_title_txv)
    TextView mTitleView;
    @BindView(R.id.chapter_header_tags_txv)
    TextView mTagsView;

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
                ab.setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
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
                            if (mPresenter != null && mFictionDetailModel != null) {
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
            case R.id.menu_edit:
                BasicCreateActivity.start(this, mFictionDetailModel);
                break;
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
            case R.id.menu_delete:
                if (mPresenter != null) {
                    OperationRequest request = new OperationRequest();
                    request.setIfiction_id(mFictionId);
                    request.setLanguage(mLanguage);
                    request.setOp(Constants.OPERATION_DROP);
                    mPresenter.operateFiction(request);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFictionDetail(FictionDetailModel fictionDetailModel) {
        mTitleView.setText(fictionDetailModel.getTitle());
        mTagsView.setText(fictionDetailModel.getTags().get(0));
        ImageLoaderUtils.getInstance().loadImage(this, fictionDetailModel.getCover(), mCoverImage, R.color.colorPrimaryDark);

        String author = TextUtils.isEmpty(fictionDetailModel.getWriter().getName()) ? getString(R.string.app_name) : fictionDetailModel.getWriter().getName();
        ((TextView) mHeaderView.findViewById(R.id.chapter_header_author_txv)).setText(author);
        ((TextView) mHeaderView.findViewById(R.id.chapter_header_describe_txv)).setText(fictionDetailModel.getSummary());

        mChaptersView = mHeaderView.findViewById(R.id.chapter_header_chapters_txv);
        TextView addChapterView = (mHeaderView.findViewById(R.id.chapter_header_add_chapters_txv));
        addChapterView.setOnClickListener(this);
        setChaptersView();
    }

    private void setChaptersView() {
        if (mChaptersView != null) {
            mChaptersView.setText(getString(R.string.chapter_total_chapters, mDatas.size()));
            if (mDatas.size() <= 0 || mFictionDetailModel.getSerials()) {
                mHeaderView.findViewById(R.id.chapter_header_add_chapters_txv).setVisibility(View.VISIBLE);
            } else {
                mHeaderView.findViewById(R.id.chapter_header_add_chapters_txv).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chapter_header_add_chapters_txv:
                FictionModel model = new FictionModel();
                model.setIfiction_id(mFictionId);
                model.setLanguage(mLanguage);
                model.setNum(mDatas.size() + 1);
                model.setStatus(Constants.STATUS_CREATING);
                //model.setName(getString(R.string.chapter_num, mDatas.size() + 1, ""));
                if (mFictionDetailModel != null) {
                    model.setName(mFictionDetailModel.getTitle());
                }
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
            setChaptersView();
        } else {
            Toast.makeText(this, R.string.common_get_info_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showFictionModel(FictionModel model) {
        mLoadingView.setVisibility(View.GONE);
        CreateActivity.start(this, model);
    }

    @Override
    public void operateFictionFinished(boolean success, String operation) {
        Toast.makeText(this, success ? R.string.common_update_info_success : R.string.common_update_info_failed, Toast.LENGTH_SHORT).show();
        if (Constants.OPERATION_DROP.equals(operation)) {
            EventBus.getDefault().post(Constants.RELOAD_DATA_AFTER_LOGINED);
            finish();
        } else {
            if (mPresenter != null) {
                mPresenter.getFictionDetail(mFictionId, mLanguage);
            }
        }
    }

    @Override
    public void operateChapterFinished(boolean success) {
        Toast.makeText(this, success ? R.string.common_update_info_success : R.string.common_update_info_failed, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, R.string.creation_detail_release_failed, Toast.LENGTH_SHORT).show();
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
        TextView dropView = view.findViewById(R.id.dialog_chapter_drop_txv);
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getStatus() == Constants.STATUS_CREATING) {
                    CreateActivity.start(CreationDetailActivity.this, model);
                } else {
                    if (mPresenter != null && mFictionDetailModel != null) {
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
        dropView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        request.setOp(Constants.OPERATION_DROP);
                        mPresenter.operateChapter(request);
                    }
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
