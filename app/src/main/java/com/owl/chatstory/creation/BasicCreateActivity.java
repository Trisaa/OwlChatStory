package com.owl.chatstory.creation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.CameraUtils;
import com.owl.chatstory.common.util.FileUtils;
import com.owl.chatstory.common.util.FirebaseUtil;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.homesource.model.CategoryModel;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.Subscribe;

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

    private BasicCreateContract.Presenter mPresenter;
    private String mCoverImagePath;
    private String mCategory;

    public static void start(Context context) {
        Intent intent = new Intent(context, BasicCreateActivity.class);
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
        new BasicCreatePresenter(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:
                    CameraUtils.cropPhoto(this, data.getData(),9,16);
                    break;
                case REQUEST_CODE_CAMERA:
                    CameraUtils.cropPhoto(this, FileUtils.getFileUri(this, FileUtils.getFilePath("temp.jpg")),9,16);
                    break;
                case UCrop.REQUEST_CROP:
                    mCoverImagePath = UCrop.getOutput(data).getPath();
                    ImageLoaderUtils.getInstance().loadImage(this, mCoverImagePath, mCoverImg, R.color.colorPrimaryDark);
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.create_single_txv)
    public void createSingle() {
        CreateActivity.start(this);
    }

    @OnClick(R.id.create_serialized_txv)
    public void createSerialized() {
        saveData();
    }

    @OnClick(R.id.create_cover_img)
    public void uploadCover() {
        showSelectDialog();
    }

    private void saveData() {
        String title = mTitleEdit.getText().toString();
        String summary = mDescribeEdit.getText().toString();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(summary)
                || TextUtils.isEmpty(mCategory) || TextUtils.isEmpty(mCoverImagePath)) {
            Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
        } else {
            final FictionDetailModel fictionDetailModel = new FictionDetailModel();
            fictionDetailModel.setTitle(title);
            fictionDetailModel.setSummary(summary);
            //fictionDetailModel.setTags();
            FirebaseUtil.upLoadFile(mCoverImagePath, new FirebaseUtil.OnUploadListener() {
                @Override
                public void onFailure() {
                    Toast.makeText(BasicCreateActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                    CreateActivity.start(BasicCreateActivity.this);
                }

                @Override
                public void onSuccess(String url) {
                    fictionDetailModel.setCover(url);
                    CreateActivity.start(BasicCreateActivity.this);
                }
            });
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
    public void showProgress(boolean show) {

    }
}
