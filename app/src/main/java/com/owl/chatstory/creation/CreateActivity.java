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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.chat.adapter.ChatAsideDelegate;
import com.owl.chatstory.chat.adapter.ChatLeftDelegate;
import com.owl.chatstory.chat.adapter.ChatRightDelegate;
import com.owl.chatstory.chat.adapter.ReadItemDecoration;
import com.owl.chatstory.common.util.CameraUtils;
import com.owl.chatstory.common.util.Constants;
import com.owl.chatstory.common.util.DeviceUtils;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.util.FileUtils;
import com.owl.chatstory.common.util.FirebaseUtil;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.view.MaxWidthRecyclerView;
import com.owl.chatstory.creation.adapter.ItemTouchCallback;
import com.owl.chatstory.creation.adapter.RoleItemDecoration;
import com.owl.chatstory.data.chatsource.model.ActorModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.MessageModel;
import com.owl.chatstory.data.eventsource.CreationDetailEvent;
import com.yalantis.ucrop.UCrop;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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

public class CreateActivity extends BaseActivity implements CreateContract.View {
    private static final String EXTRA_FICTION_ID = "EXTRA_FICTION_ID";
    private static final String EXTRA_FICTION_MODEL = "EXTRA_FICTION_MODEL";
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
    @BindView(R.id.common_progressbar_layout)
    View mLoadingView;
    @BindView(R.id.toolbar_title)
    TextView mTitleView;

    private CreateContract.Presenter mPresenter;
    private ImageView mDialogUserIcon;
    private String mImagePath, mChapterTitle;
    private ActorModel mFirstRole, mAsideRole, mCurrentRole;
    private List<ActorModel> mSecondRoleList = new ArrayList<>();
    private List<ActorModel> mRoleList = new ArrayList<>();
    private List<MessageModel> mMessageList = new ArrayList<>();
    private CommonAdapter<ActorModel> mRolesAdapter;
    private CommonAdapter<ActorModel> mEditRolesAdapter;
    private MultiItemTypeAdapter<MessageModel> mAdapter;
    private FictionModel mFictionModel;

    public static void start(Context context, FictionModel model) {
        Intent intent = new Intent(context, CreateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FICTION_MODEL, model);
        intent.putExtras(bundle);
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
                ab.setDisplayShowTitleEnabled(false);
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_preview:
                if (mMessageList.size() > 0) {
                    PreviewActivity.start(this, (ArrayList) mMessageList);
                }
                break;
            case R.id.menu_done:
                DialogUtils.showDialog(this, R.string.create_publish_chapter
                        , R.string.create_dialog_cancel, R.string.create_dialog_ok
                        , new DialogUtils.OnDialogClickListener() {
                            @Override
                            public void onOK() {
                                if (mPresenter != null) {
                                    mFictionModel.setName(mChapterTitle);
                                    mFictionModel.setList(getRequestMessageList());
                                    mPresenter.publishChapter(mFictionModel);
                                }
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
        mFictionModel = getIntent().getParcelableExtra(EXTRA_FICTION_MODEL);
        if (mFictionModel != null) {
            if (mFictionModel.getList() != null) {
                mMessageList.clear();
                mMessageList.addAll(mFictionModel.getList());
            }
            if (!TextUtils.isEmpty(mFictionModel.getName())) {
                mChapterTitle = mFictionModel.getName();
                mTitleView.setText(mChapterTitle);
            } else {
                mTitleView.setText("");
            }
        }
        mAsideRole = new ActorModel(ActorModel.ROLE_ASIDE, "", DeviceUtils.getUri(R.mipmap.create_aside_role));
        mAsideRole.setId("");
        initRoleAdapter();
        initMessageAdapter();
        new CreatePresenter(this);
    }

    @Override
    public void onBackPressed() {
        if (mMessageList.size() > 0) {
            if (mFictionModel.getStatus() == Constants.STATUS_CREATING) {
                DialogUtils.showDialog(this, R.string.create_save_chapter
                        , R.string.create_dialog_not_save, R.string.create_dialog_ok
                        , new DialogUtils.OnDialogClickListener() {
                            @Override
                            public void onOK() {
                                if (mPresenter != null) {
                                    mFictionModel.setName(mChapterTitle);
                                    mFictionModel.setList(mMessageList);
                                    mFictionModel.setStatus(Constants.STATUS_CREATING);
                                    mPresenter.saveChapter(mFictionModel);
                                    EventBus.getDefault().post(new CreationDetailEvent(true));
                                }
                                finish();
                            }

                            @Override
                            public void onCancel() {
                                finish();
                            }
                        });
            } else {
                DialogUtils.showDialog(this, R.string.create_publish_chapter
                        , R.string.create_dialog_cancel, R.string.create_dialog_ok
                        , new DialogUtils.OnDialogClickListener() {
                            @Override
                            public void onOK() {
                                if (mPresenter != null) {
                                    mFictionModel.setName(mChapterTitle);
                                    mFictionModel.setList(getRequestMessageList());
                                    mPresenter.publishChapter(mFictionModel);
                                }
                            }

                            @Override
                            public void onCancel() {
                                finish();
                            }
                        });
            }
        } else {
            super.onBackPressed();
        }
    }

    private List<MessageModel> getRequestMessageList() {
        List<MessageModel> list = new ArrayList<>();
        list.addAll(mMessageList);
        for (MessageModel model : list) {
            model.setAvatar(null);
            model.setActor(null);
        }
        return list;
    }

    private void initRoleAdapter() {
        mRolesAdapter = new CommonAdapter<ActorModel>(this, R.layout.create_role_item, mSecondRoleList) {
            @Override
            protected void convert(ViewHolder holder, ActorModel userModel, int position) {
                ImageLoaderUtils.getInstance().loadCircleImage(CreateActivity.this, userModel.getPicture(), (ImageView) holder.getView(R.id.create_role_item_img), R.mipmap.user_default_icon);
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

        mEditRolesAdapter = new CommonAdapter<ActorModel>(this, R.layout.create_role_item, mRoleList) {
            @Override
            protected void convert(ViewHolder holder, ActorModel userModel, int position) {
                ImageLoaderUtils.getInstance().loadCircleImage(CreateActivity.this, userModel.getPicture(), (ImageView) holder.getView(R.id.create_role_item_img));
                holder.setVisible(R.id.create_role_item_img_edit, true);
            }
        };
        mEditRolesAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                showUpdateRoleDialog(position);
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchCallback(new ItemTouchCallback.ItemTouchListener() {
            @Override
            public void onItemMoved(int fromPosition, int toPosition) {
                Collections.swap(mMessageList, fromPosition, toPosition);
                mAdapter.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemDismissd(int position) {
                mMessageList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        }));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
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
                    mImagePath = UCrop.getOutput(data).getPath();
                    ImageLoaderUtils.getInstance().loadCircleImage(this, mImagePath, mDialogUserIcon);
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
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

    private void showAddRoleDialog(final int role) {
        mImagePath = null;
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_role, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        mDialogUserIcon = (ImageView) view.findViewById(R.id.add_role_img);
        final EditText editText = (EditText) view.findViewById(R.id.add_role_edit);
        view.findViewById(R.id.add_role_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog();
            }
        });
        view.findViewById(R.id.add_role_delete_txv).setVisibility(View.GONE);
        view.findViewById(R.id.add_role_submit_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && mImagePath != null) {
                    if (role == ActorModel.ROLE_FIRST) {
                        FirebaseUtil.upLoadFile(mImagePath, new FirebaseUtil.OnUploadListener() {
                            @Override
                            public void onFailure() {
                                mLoadingView.setVisibility(View.GONE);
                                Toast.makeText(CreateActivity.this, R.string.common_upload_pic_failed, Toast.LENGTH_SHORT).show();
                                mFirstRole = new ActorModel(ActorModel.ROLE_FIRST, name, DeviceUtils.getUri(R.mipmap.user_default_icon));
                                updateRoleList();
                            }

                            @Override
                            public void onSuccess(String url) {
                                mFirstRole = new ActorModel(ActorModel.ROLE_FIRST, name, url);
                                updateRoleList();
                                //updateFirstRole(mFirstRole.getIcon());
                            }
                        });
                    } else {
                        FirebaseUtil.upLoadFile(mImagePath, new FirebaseUtil.OnUploadListener() {
                            @Override
                            public void onFailure() {
                                mLoadingView.setVisibility(View.GONE);
                                Toast.makeText(CreateActivity.this, R.string.common_upload_pic_failed, Toast.LENGTH_SHORT).show();
                                ActorModel userModel = new ActorModel(ActorModel.ROLE_SECOND, name, DeviceUtils.getUri(R.mipmap.user_default_icon));
                                mSecondRoleList.add(userModel);
                                updateRoleList();
                            }

                            @Override
                            public void onSuccess(String url) {
                                ActorModel userModel = new ActorModel(ActorModel.ROLE_SECOND, name, url);
                                mSecondRoleList.add(userModel);
                                updateRoleList();
                                //mRolesAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    alertDialog.dismiss();
                    mLoadingView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(CreateActivity.this, R.string.common_invalid_info, Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

    private void showUpdateRoleDialog(final int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_role, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        final ActorModel userModel = mRoleList.get(position);
        final String name = userModel.getName();
        final String icon = userModel.getPicture();
        mImagePath = icon;

        mDialogUserIcon = (ImageView) view.findViewById(R.id.add_role_img);
        final EditText editText = (EditText) view.findViewById(R.id.add_role_edit);
        TextView textView = (TextView) view.findViewById(R.id.add_role_submit_txv);
        editText.setText(name);
        textView.setText(R.string.common_update);
        ImageLoaderUtils.getInstance().loadCircleImage(this, icon, mDialogUserIcon);
        mDialogUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog();
            }
        });
        view.findViewById(R.id.add_role_delete_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRoleList.remove(position);
                mEditRolesAdapter.notifyItemRemoved(position);
                updateRoleInfo(userModel, true);
                alertDialog.dismiss();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String temp = editText.getText().toString().trim();
                //什么都没改
                if (name.equals(temp) && mImagePath.equals(icon)) {
                    alertDialog.dismiss();
                } else {
                    if (!TextUtils.isEmpty(temp)) {
                        //仅修改名称
                        if (mImagePath.equals(icon)) {
                            userModel.setName(temp);
                            userModel.setPicture(mImagePath);
                            mEditRolesAdapter.notifyItemChanged(position);
                            updateRoleInfo(userModel, false);
                        } else {
                            FirebaseUtil.upLoadFile(mImagePath, new FirebaseUtil.OnUploadListener() {
                                @Override
                                public void onFailure() {
                                    mLoadingView.setVisibility(View.GONE);
                                    Toast.makeText(CreateActivity.this, R.string.common_upload_pic_failed, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(String url) {
                                    userModel.setName(temp);
                                    userModel.setPicture(url);
                                    mEditRolesAdapter.notifyItemChanged(position);
                                    updateRoleInfo(userModel, false);
                                }
                            });
                        }
                        alertDialog.dismiss();
                        mLoadingView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(CreateActivity.this, R.string.common_invalid_info, Toast.LENGTH_SHORT).show();
                    }
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

    private void updateRoleInfo(ActorModel model, boolean deleteOrUpdate) {
        if (model.getRole_type() == ActorModel.ROLE_FIRST) {
            if (deleteOrUpdate) {
                //删除主角
                deleteMessageAboutUser(model);
                updateFirstRole(DeviceUtils.getUri(R.mipmap.user_default_icon));
                mFirstRole = null;
            } else {
                //更新主角
                updateMessageAboutUser(model);
                updateFirstRole(model.getPicture());
                mFirstRole = model;
            }
        } else {
            if (deleteOrUpdate) {
                //删除配角
                deleteMessageAboutUser(model);
                for (ActorModel userModel : mSecondRoleList) {
                    if (userModel.getId().equals(model.getId())) {
                        mSecondRoleList.remove(userModel);
                        mRolesAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            } else {
                //更新配角
                updateMessageAboutUser(model);
                for (int i = 0; i < mSecondRoleList.size(); i++) {
                    ActorModel userModel = mSecondRoleList.get(i);
                    if (userModel.getId().equals(model.getId())) {
                        userModel.setName(model.getName());
                        userModel.setPicture(model.getPicture());
                        mRolesAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        }
        if (!deleteOrUpdate) {
            updateRoleList();
        }
        mCurrentRole = null;
        ImageLoaderUtils.getInstance().loadCircleImage(this, DeviceUtils.getUri(R.mipmap.user_default_icon), mCurrentRoleImg);
    }

    /**
     * 删除所有跟user有关的对话
     *
     * @param model
     */
    private void deleteMessageAboutUser(ActorModel model) {
        Iterator<MessageModel> it = mMessageList.iterator();
        while (it.hasNext()) {
            MessageModel x = it.next();
            if (x.getId().equals(model.getId())) {
                it.remove();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 更新所有跟user有关的对话
     */
    private void updateMessageAboutUser(ActorModel model) {
        for (int i = 0; i < mMessageList.size(); i++) {
            MessageModel messageModel = mMessageList.get(i);
            if (messageModel.getId() != null && messageModel.getId().equals(model.getId())) {
                messageModel.setActor(model.getName());
                messageModel.setAvatar(model.getPicture());
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void updateRole(ActorModel userModel) {
        mCurrentRole = userModel;
        ImageLoaderUtils.getInstance().loadCircleImage(this, mCurrentRole.getPicture(), mCurrentRoleImg);
    }

    private void updateFirstRole(String url) {
        ImageLoaderUtils.getInstance().loadCircleImage(this, url, mFirstRoleImg, R.mipmap.user_default_icon);
    }

    private void getEditRoleList() {
        if (mRoleList != null) {
            mRoleList.clear();
        }
        mRoleList.addAll(mSecondRoleList);
        if (mFirstRole != null) {
            mRoleList.add(mRoleList.size(), mFirstRole);
        }
    }

    /**
     * 更新角色信息到服务端
     */
    private void updateRoleList() {
        getEditRoleList();
        if (mPresenter != null) {
            mPresenter.updateRoleList(mFictionModel.getIfiction_id(), mFictionModel.getLanguage(), mRoleList);
        }
    }

    @OnClick(R.id.create_first_role_img)
    public void clickFirstRole() {
        if (mFirstRole != null) {
            updateRole(mFirstRole);
        } else {
            showAddRoleDialog(ActorModel.ROLE_FIRST);
        }
    }

    @OnClick(R.id.create_second_role_img)
    public void clickSecondRole() {
        showAddRoleDialog(ActorModel.ROLE_SECOND);
    }

    @OnClick(R.id.create_aside_role_img)
    public void clickAsideRole() {
        updateRole(mAsideRole);
    }

    @OnClick(R.id.create_edit_role_img)
    public void clickEditRole() {
        getEditRoleList();
        mEditRolesAdapter.notifyDataSetChanged();
        mEditRoleLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.toolbar_title)
    public void clickToolbarTitle() {
        showAddChapterDialog(mChapterTitle);
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
                messageModel.setId(mCurrentRole.getId());
                messageModel.setActor(mCurrentRole.getName());
                messageModel.setAvatar(mCurrentRole.getPicture());
                messageModel.setWord(message);
                messageModel.setLocation(mCurrentRole.getRoleTypeStr());
                mMessageList.add(messageModel);
                mAdapter.notifyItemInserted(mMessageList.size() - 1);
                mRecyclerView.smoothScrollToPosition(mMessageList.size() - 1);
                mEditText.setText("");
            } else {
                Toast.makeText(this, R.string.create_empty_message, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.create_choose_roles, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void publishSuccess() {
        EventBus.getDefault().post(new CreationDetailEvent(true));
        Toast.makeText(this, R.string.common_update_info_success, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showRoleList(List<ActorModel> list) {
        mLoadingView.setVisibility(View.GONE);
        if (list != null) {
            mSecondRoleList.clear();
            for (int i = 0; i < list.size(); i++) {
                ActorModel userModel = list.get(i);
                if (userModel.getRole_type() == ActorModel.ROLE_FIRST) {
                    mFirstRole = userModel;
                    updateFirstRole(mFirstRole.getPicture());
                } else {
                    mSecondRoleList.add(userModel);
                }
            }
            mRolesAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, R.string.common_get_info_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void publishFailed() {
        Toast.makeText(this, R.string.creation_detail_release_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(CreateContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getRoleList(mFictionModel.getIfiction_id(), mFictionModel.getLanguage());
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
                    mChapterTitle = title;
                    mTitleView.setText(mChapterTitle);
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
