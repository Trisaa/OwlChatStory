package com.tap.chatstory.user.info;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tap.chatstory.R;
import com.tap.chatstory.base.BaseActivity;
import com.tap.chatstory.common.util.CameraUtils;
import com.tap.chatstory.common.util.DialogUtils;
import com.tap.chatstory.common.util.FileUtils;
import com.tap.chatstory.common.util.FirebaseUtil;
import com.tap.chatstory.common.util.ImageLoaderUtils;
import com.tap.chatstory.data.usersource.model.UserModel;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tap.chatstory.common.util.CameraUtils.REQUEST_CODE_CAMERA;
import static com.tap.chatstory.common.util.CameraUtils.REQUEST_CODE_GALLERY;
import static com.tap.chatstory.creation.BasicCreateActivity.PERMISSIONS_REQUEST_CAMERA;
import static com.tap.chatstory.creation.BasicCreateActivity.PERMISSIONS_REQUEST_WRITE_STORAGE;

/**
 * Created by lebron on 2018/1/29.
 */

public class EditUserActivity extends BaseActivity implements EditUserContract.View {
    private static final String EXTRA_USER_MODEL = "EXTRA_USER_MODEL";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_edit_icon_img)
    ImageView mIconView;
    @BindView(R.id.user_edit_name)
    EditText mNameEdit;
    @BindView(R.id.user_edit_summary)
    EditText mSummaryEdit;
    @BindView(R.id.user_edit_gender_txv)
    TextView mGenderView;
    @BindView(R.id.common_progressbar_layout)
    View mLoadingView;

    private String mIconPath;
    private EditUserContract.Presenter mPresenter;
    private UserModel mUserModel;

    public static void start(Context context, UserModel userModel) {
        Intent intent = new Intent(context, EditUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_USER_MODEL, userModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_edit_user;
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
        FileUtils.deleteFile(mIconPath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_ok_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_done) {
            mLoadingView.setVisibility(View.VISIBLE);
            if (mUserModel != null) {
                if (!TextUtils.isEmpty(mNameEdit.getText().toString())) {
                    mUserModel.setName(mNameEdit.getText().toString());
                }
                if (!TextUtils.isEmpty(mSummaryEdit.getText().toString())) {
                    mUserModel.setSummary(mSummaryEdit.getText().toString());
                }
                if (mIconPath.equals(mUserModel.getIcon())) {
                    mPresenter.updateUserInfo(mUserModel);
                } else {
                    FirebaseUtil.upLoadFile(mIconPath, new FirebaseUtil.OnUploadListener() {
                        @Override
                        public void onFailure() {
                            Toast.makeText(EditUserActivity.this, R.string.common_upload_pic_failed, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(String url) {
                            mUserModel.setIcon(url);
                            mPresenter.updateUserInfo(mUserModel);
                        }
                    });
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewsAndData() {
        mUserModel = getIntent().getParcelableExtra(EXTRA_USER_MODEL);
        updateUserInfo();
        new EditUserPresenter(this);
    }

    private void updateUserInfo() {
        if (mUserModel != null) {
            mIconPath = mUserModel.getIcon();
            ImageLoaderUtils.getInstance().loadCircleImage(this, mIconPath, mIconView, R.mipmap.user_default_icon);
            mNameEdit.setText(mUserModel.getName());
            mSummaryEdit.setText(mUserModel.getSummary());
            mGenderView.setText(mUserModel.getGender() == 0 ? R.string.common_female : R.string.common_male);
        }
    }

    @OnClick(R.id.user_edit_icon_img)
    public void clickIcon() {
        showSelectDialog();
    }

    @OnClick(R.id.user_edit_gender_choose_layout)
    public void clickGender() {
        if (mUserModel == null) {
            return;
        }
        DialogUtils.showGenderDialog(this, mUserModel.getGender(), new DialogUtils.OnGenderChooseListener() {
            @Override
            public void onChoose(int gender) {
                mUserModel.setGender(gender);
                updateUserInfo();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:
                    CameraUtils.cropPhoto(this, data.getData(), 1, 1);
                    break;
                case REQUEST_CODE_CAMERA:
                    CameraUtils.cropPhoto(this, FileUtils.getFileUri(this, FileUtils.getFilePath("temp.jpg")), 1, 1);
                    break;
                case UCrop.REQUEST_CROP:
                    mIconPath = UCrop.getOutput(data).getPath();
                    ImageLoaderUtils.getInstance().loadCircleImage(this, mIconPath, mIconView, R.mipmap.user_default_icon);
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
    public void setPresenter(EditUserContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showUserInfo() {
        mLoadingView.setVisibility(View.GONE);
        EventBus.getDefault().post(mUserModel);
        finish();
    }

    @Override
    public void updateFailed() {
        mLoadingView.setVisibility(View.GONE);
        Toast.makeText(this, R.string.common_network_error, Toast.LENGTH_SHORT).show();
    }
}
