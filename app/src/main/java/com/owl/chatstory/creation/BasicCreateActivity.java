package com.owl.chatstory.creation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.CameraUtils;
import com.owl.chatstory.common.util.Constants;
import com.owl.chatstory.common.util.DeviceUtils;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.util.FileUtils;
import com.owl.chatstory.common.util.FirebaseUtil;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.homesource.model.CategoryModel;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.owl.chatstory.common.util.CameraUtils.REQUEST_CODE_CAMERA;
import static com.owl.chatstory.common.util.CameraUtils.REQUEST_CODE_GALLERY;

/**
 * Created by lebron on 2017/10/31.
 */

public class BasicCreateActivity extends BaseActivity implements BasicCreateContract.View {
    public static final int PERMISSIONS_REQUEST_WRITE_STORAGE = 100;
    public static final int PERMISSIONS_REQUEST_CAMERA = 200;
    public static final String EXTRA_FICTION_DETAIL = "EXTRA_FICTION_DETAIL";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.create_cover_img)
    ImageView mCoverImg;
    @BindView(R.id.create_title_edit)
    EditText mTitleEdit;
    @BindView(R.id.create_description_edit)
    EditText mDescribeEdit;
    @BindView(R.id.create_category_choose_txv)
    TextView mCategoryView;
    @BindView(R.id.create_language_choose_txv)
    TextView mLanguageView;
    @BindView(R.id.create_layout)
    LinearLayout mBottomLayout;
    @BindView(R.id.create_update_fiction_txv)
    TextView mUpdateView;
    @BindView(R.id.common_progressbar_layout)
    View mLoadingView;

    private BasicCreateContract.Presenter mPresenter;
    private String mCoverImagePath;
    private String mCategory, mLanguage;
    private FictionDetailModel mFictionDetailModel;
    private boolean isUpdateOrAdd;

    public static void start(Context context, FictionDetailModel model) {
        Intent intent = new Intent(context, BasicCreateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FICTION_DETAIL, model);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_basic_create;
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
        mFictionDetailModel = getIntent().getParcelableExtra(EXTRA_FICTION_DETAIL);
        isUpdateOrAdd = mFictionDetailModel != null;
        mLanguage = Constants.getLanguage();
        if (isUpdateOrAdd) {
            mCoverImagePath = mFictionDetailModel.getCover();
            mCategory = mFictionDetailModel.getTags().get(0);
            mLanguage = mFictionDetailModel.getLanguage();
            mBottomLayout.setVisibility(View.GONE);
            mUpdateView.setVisibility(View.VISIBLE);
            ImageLoaderUtils.getInstance().loadImage(this, mFictionDetailModel.getCover(), mCoverImg, R.color.colorPrimaryDark);
            mTitleEdit.setText(mFictionDetailModel.getTitle());
            mTitleEdit.setSelection(mFictionDetailModel.getTitle().length());
            mDescribeEdit.setText(mFictionDetailModel.getSummary());
            mCategoryView.setText(mCategory);
            mLanguageView.setText(Constants.getLanguage(mLanguage));
            mLanguageView.setEnabled(false);
        } else {
            mLanguageView.setText(Constants.getLanguage(mLanguage));
        }
        new BasicCreatePresenter(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:
                    CameraUtils.cropPhoto(this, data.getData(), 5, 7);
                    break;
                case REQUEST_CODE_CAMERA:
                    CameraUtils.cropPhoto(this, FileUtils.getFileUri(this, FileUtils.getFilePath("temp.jpg")), 5, 7);
                    break;
                case UCrop.REQUEST_CROP:
                    mCoverImagePath = UCrop.getOutput(data).getPath();
                    ImageLoaderUtils.getInstance().loadImage(this, mCoverImagePath, mCoverImg, R.color.colorPrimaryDark);
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, R.string.common_get_pic_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CameraUtils.chooseFromGallery(this);
            }
            return;
        } else if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CameraUtils.chooseFromCamera(this);
            }
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.deleteFile(mCoverImagePath);
        mPresenter.unsubscribe();
    }

    @Subscribe
    public void getCategoryEvent(CategoryModel model) {
        if (model != null) {
            mCategory = model.getTitle();
            mCategoryView.setText(mCategory);
        }
    }

    @OnClick(R.id.create_category_choose_layout)
    public void chooseCategory() {
        ChooseCategoryActivity.start(this);
    }

    @OnClick(R.id.create_language_choose_txv)
    public void chooseLanguage() {
        DialogUtils.showLanguageDialog(this, new DialogUtils.OnLanguageChooseListener() {
            @Override
            public void onChoose(String language) {
                mLanguage = language;
                mLanguageView.setText(Constants.getLanguage(language));
            }
        }, mLanguage);
    }

    @OnClick(R.id.create_single_txv)
    public void createSingle() {
        saveData(false);
    }

    @OnClick(R.id.create_serialized_txv)
    public void createSerialized() {
        saveData(true);
    }

    @OnClick(R.id.create_cover_img)
    public void uploadCover() {
        showSelectDialog();
    }

    @OnClick(R.id.create_update_fiction_txv)
    public void updateInfo() {
        saveData(mFictionDetailModel.getSerials());
    }

    private void saveData(boolean serials) {
        String title = mTitleEdit.getText().toString();
        String summary = mDescribeEdit.getText().toString();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(summary)
                || TextUtils.isEmpty(mCategory) || TextUtils.isEmpty(mCoverImagePath)
                || TextUtils.isEmpty(mLanguage)) {
            Toast.makeText(this, R.string.common_invalid_info, Toast.LENGTH_SHORT).show();
        } else {
            if (mFictionDetailModel == null) {
                mFictionDetailModel = new FictionDetailModel();
            }
            mFictionDetailModel.setTitle(title);
            mFictionDetailModel.setSummary(summary);
            mFictionDetailModel.setLanguage(mLanguage);
            mFictionDetailModel.setSerials(serials);
            List<String> list = new ArrayList<>();
            list.add(mCategory);
            mFictionDetailModel.setTags(list);
            //未更改头像
            if (mCoverImagePath.equals(mFictionDetailModel.getCover())) {
                mPresenter.saveFictionBasicInfo(mFictionDetailModel);
            } else {
                showLoadingView(true);
                FirebaseUtil.upLoadFile(mCoverImagePath, new FirebaseUtil.OnUploadListener() {
                    @Override
                    public void onFailure() {
                        Toast.makeText(BasicCreateActivity.this, R.string.common_upload_pic_failed, Toast.LENGTH_SHORT).show();
                        mFictionDetailModel.setCover(DeviceUtils.getUri(R.mipmap.create_default_cover));
                        mPresenter.saveFictionBasicInfo(mFictionDetailModel);
                    }

                    @Override
                    public void onSuccess(String url) {
                        mFictionDetailModel.setCover(url);
                        mPresenter.saveFictionBasicInfo(mFictionDetailModel);
                    }
                });
            }
        }
    }

    private void showSelectDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.common_update_photo, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.account_select_gallery_photo_Llyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_WRITE_STORAGE);
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.account_select_camera_photo_Llyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionGranted(Manifest.permission.CAMERA, PERMISSIONS_REQUEST_CAMERA);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void checkPermissionGranted(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            if (requestCode == PERMISSIONS_REQUEST_WRITE_STORAGE) {
                CameraUtils.chooseFromGallery(this);
            } else if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
                CameraUtils.chooseFromCamera(this);
            }
        }
    }

    @Override
    public void setPresenter(BasicCreateContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadingView(boolean show) {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess(FictionDetailModel model) {
        if (isUpdateOrAdd) {
            EventBus.getDefault().post(mFictionDetailModel);
            finish();
        } else {
            FictionModel fictionModel = new FictionModel();
            fictionModel.setLanguage(mLanguage);
            fictionModel.setIfiction_id(model.getId());
            fictionModel.setNum(1);
            fictionModel.setName(getString(R.string.chapter_num,1,""));
            fictionModel.setStatus(Constants.STATUS_CREATING);
            CreateActivity.start(BasicCreateActivity.this, fictionModel);
            finish();
        }
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, R.string.common_update_info_failed, Toast.LENGTH_SHORT).show();
        finish();
    }
}
