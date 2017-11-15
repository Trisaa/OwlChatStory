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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.chat.adapter.ChatAsideDelegate;
import com.owl.chatstory.chat.adapter.ChatLeftDelegate;
import com.owl.chatstory.chat.adapter.ChatRightDelegate;
import com.owl.chatstory.chat.adapter.ReadItemDecoration;
import com.owl.chatstory.common.util.CameraUtils;
import com.owl.chatstory.common.util.DeviceUtils;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.util.FileUtils;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.view.MaxWidthRecyclerView;
import com.owl.chatstory.creation.adapter.RoleItemDecoration;
import com.owl.chatstory.data.chatsource.model.MessageModel;
import com.owl.chatstory.data.usersource.model.UserModel;
import com.yalantis.ucrop.UCrop;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.owl.chatstory.common.util.CameraUtils.REQUEST_CODE_CAMERA;
import static com.owl.chatstory.common.util.CameraUtils.REQUEST_CODE_GALLERY;
import static com.owl.chatstory.creation.BasicCreateActivity.PERMISSIONS_REQUEST_CAMERA;
import static com.owl.chatstory.creation.BasicCreateActivity.PERMISSIONS_REQUEST_WRITE_STORAGE;

/**
 * Created by lebron on 2017/11/3.
 */

public class CreateActivity extends BaseActivity {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.create_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.create_role_img)
    ImageView mCurrentRoleImg;
    @BindView(R.id.create_first_role_img)
    ImageView mFirstRoleImg;
    @BindView(R.id.create_second_role_img)
    ImageView mSecondRoleImg;
    @BindView(R.id.create_aside_role_img)
    ImageView mAsideRoleImg;
    @BindView(R.id.create_second_role_recycler)
    MaxWidthRecyclerView mRoleRecyclerView;
    @BindView(R.id.create_edit_role_img)
    ImageView mEditRoleImg;
    @BindView(R.id.create_message_txv)
    EditText mEditText;
    @BindView(R.id.create_edit_role_recycler)
    RecyclerView mEditRecyclerView;
    @BindView(R.id.create_edit_role_layout)
    RelativeLayout mEditRoleLayout;

    private ImageView mDialogUserIcon;
    private String mImagePath;
    private UserModel mFirstRole, mAsideRole, mCurrentRole;
    private List<UserModel> mSecondRoleList = new ArrayList<>();
    private List<UserModel> mRoleList = new ArrayList<>();
    private List<MessageModel> mMessageList = new ArrayList<>();
    private CommonAdapter<UserModel> mRolesAdapter;
    private CommonAdapter<UserModel> mEditRolesAdapter;
    private MultiItemTypeAdapter<MessageModel> mAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, CreateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_create;
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
        getMenuInflater().inflate(R.menu.create_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_preview:
                PreviewActivity.start(this, (ArrayList) mMessageList);
                break;
            case R.id.menu_done:
                DialogUtils.showDialog(this, R.string.create_publish_chapter
                        , R.string.create_dialog_cancel, R.string.create_dialog_ok
                        , new DialogUtils.OnDialogClickListener() {
                            @Override
                            public void onOK() {
                                finish();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewsAndData() {
        mAsideRole = new UserModel(UserModel.ROLE_ASIDE, "", DeviceUtils.getUri(R.mipmap.btn_pang_normal));
        initRoleAdapter();
        initMessageAdapter();
    }

    @Override
    public void onBackPressed() {
        if (mMessageList.size() > 0) {
            DialogUtils.showDialog(this, R.string.create_save_chapter
                    , R.string.create_dialog_not_save, R.string.create_dialog_ok
                    , new DialogUtils.OnDialogClickListener() {
                        @Override
                        public void onOK() {
                            finish();
                        }

                        @Override
                        public void onCancel() {
                            finish();
                        }
                    });
        } else {
            super.onBackPressed();
        }
    }

    private void initRoleAdapter() {
        mRolesAdapter = new CommonAdapter<UserModel>(this, R.layout.create_role_item, mSecondRoleList) {
            @Override
            protected void convert(ViewHolder holder, UserModel userModel, int position) {
                ImageLoaderUtils.getInstance().loadCircleImage(CreateActivity.this, userModel.getIcon(), (ImageView) holder.getView(R.id.create_role_item_img));
            }
        };
        mRolesAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                updateRole(mSecondRoleList.get(position));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRoleRecyclerView.addItemDecoration(new RoleItemDecoration(30));
        mRoleRecyclerView.setLayoutManager(layoutManager);
        mRoleRecyclerView.setAdapter(mRolesAdapter);

        mEditRolesAdapter = new CommonAdapter<UserModel>(this, R.layout.create_role_item, mRoleList) {
            @Override
            protected void convert(ViewHolder holder, UserModel userModel, int position) {
                ImageLoaderUtils.getInstance().loadCircleImage(CreateActivity.this, userModel.getIcon(), (ImageView) holder.getView(R.id.create_role_item_img));
                holder.setVisible(R.id.create_role_item_img_edit, true);
            }
        };
        mEditRolesAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mEditRecyclerView.addItemDecoration(new RoleItemDecoration(30));
        mEditRecyclerView.setLayoutManager(layoutManager2);
        mEditRecyclerView.setAdapter(mEditRolesAdapter);
    }

    private void initMessageAdapter() {
        mAdapter = new MultiItemTypeAdapter<>(this, mMessageList);
        mAdapter.addItemViewDelegate(new ChatLeftDelegate());
        mAdapter.addItemViewDelegate(new ChatRightDelegate());
        mAdapter.addItemViewDelegate(new ChatAsideDelegate());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ReadItemDecoration(24));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:
                    CameraUtils.cropPhoto(this, data.getData());
                    break;
                case REQUEST_CODE_CAMERA:
                    CameraUtils.cropPhoto(this, FileUtils.getFileUri(this, FileUtils.getFilePath("temp.jpg")));
                    break;
                case UCrop.REQUEST_CROP:
                    mImagePath = UCrop.getOutput(data).getPath();
                    ImageLoaderUtils.getInstance().loadCircleImage(this, mImagePath, mDialogUserIcon);
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
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

    private void showAddRoleDialog(final int role) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_role, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        mDialogUserIcon = (ImageView) view.findViewById(R.id.add_role_img);
        final EditText editText = (EditText) view.findViewById(R.id.add_role_edit);
        view.findViewById(R.id.add_role_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImagePath = null;
                showSelectDialog();
            }
        });
        view.findViewById(R.id.add_role_submit_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && mImagePath != null) {
                    if (role == UserModel.ROLE_FIRST) {
                        mFirstRole = new UserModel(UserModel.ROLE_FIRST, name, mImagePath);
                        updateFirstRole();
                    } else {
                        UserModel userModel = new UserModel(UserModel.ROLE_SECOND, name, mImagePath);
                        mSecondRoleList.add(userModel);
                        mRolesAdapter.notifyDataSetChanged();
                    }
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(CreateActivity.this, "信息不完善", Toast.LENGTH_SHORT).show();
                }
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

    private void updateRole(UserModel userModel) {
        mCurrentRole = userModel;
        ImageLoaderUtils.getInstance().loadCircleImage(this, mCurrentRole.getIcon(), mCurrentRoleImg);
    }

    private void updateFirstRole() {
        ImageLoaderUtils.getInstance().loadCircleImage(this, mFirstRole.getIcon(), mFirstRoleImg);
    }

    @OnClick(R.id.create_first_role_img)
    public void clickFirstRole() {
        if (mFirstRole != null) {
            updateRole(mFirstRole);
        } else {
            showAddRoleDialog(UserModel.ROLE_FIRST);
        }
    }

    @OnClick(R.id.create_second_role_img)
    public void clickSecondRole() {
        showAddRoleDialog(UserModel.ROLE_SECOND);
    }

    @OnClick(R.id.create_aside_role_img)
    public void clickAsideRole() {
        updateRole(mAsideRole);
    }

    @OnClick(R.id.create_edit_role_img)
    public void clickEditRole() {
        if (mRoleList != null) {
            mRoleList.clear();
        }
        mRoleList.addAll(mSecondRoleList);
        if (mFirstRole != null) {
            mRoleList.add(mRoleList.size(), mFirstRole);
        }
        mEditRolesAdapter.notifyDataSetChanged();
        mEditRoleLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.create_edit_role_close)
    public void clickEditClose() {
        mEditRoleLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.create_send_txv)
    public void clickSend() {
        if (mCurrentRole != null) {
            String message = mEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(message)) {
                MessageModel messageModel = new MessageModel();
                messageModel.setActor(mCurrentRole.getName());
                messageModel.setAvatar(mCurrentRole.getIcon());
                messageModel.setWord(message);
                messageModel.setLocation(mCurrentRole.getRoleType());
                mMessageList.add(messageModel);
                mAdapter.notifyItemInserted(mMessageList.size() - 1);
                mEditText.setText("");
            } else {
                Toast.makeText(this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "选择一个角色", Toast.LENGTH_SHORT).show();
        }
    }
}
