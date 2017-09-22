package com.owl.chatstory.common.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.owl.chatstory.R;
import com.owl.chatstory.data.homesource.model.ShareModel;
import com.owl.chatstory.data.homesource.model.UpdateModel;

/**
 * Created by lebron on 2017/9/14.
 */

public class DialogUtils {

    public static void showShareDialog(final Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(activity, R.style.NoBackGroundDialog).create();
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View dialogView = layoutInflater.inflate(R.layout.dialog_share_layout, null);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        alertDialog.show();
        alertDialog.setContentView(dialogView);
        dialogView.findViewById(R.id.share_facebook_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareModel shareModel = new ShareModel();
                shareModel.setContent("Test");
                ShareUtils.shareToFacebook(activity, null, shareModel);
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.share_twitter_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareModel shareModel = new ShareModel();
                shareModel.setContent("Test\n");
                ShareUtils.shareToTwitter(activity, shareModel);
                alertDialog.dismiss();
            }
        });
    }

    public static void showDialog(final Activity activity, int title, int negative, int positive
            , final OnDialogClickListener listener) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle(title)
                .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onCancel();
                    }
                })
                .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onOK();
                    }
                }).setCancelable(false).create();
        alertDialog.show();
    }

    public static void showUpdateDialog(final Activity activity, final UpdateModel updateModel, final OnDialogClickListener listener) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle(R.string.update_title)
                .setMessage(updateModel.getContent())
                .setNegativeButton(R.string.update_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (updateModel.getForceVersion() > DeviceUtils.getVersionCode(activity)) {
                            Toast.makeText(activity, R.string.update_force_to_update, Toast.LENGTH_SHORT).show();
                            activity.finish();
                        }
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.update_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JumpUtils.jumpToBrowser(updateModel.getPath());
                        dialog.dismiss();
                    }
                }).setCancelable(false).create();
        alertDialog.show();
    }

    public interface OnDialogClickListener {
        void onOK();

        void onCancel();
    }
}