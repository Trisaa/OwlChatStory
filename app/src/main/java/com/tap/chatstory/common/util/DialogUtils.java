package com.tap.chatstory.common.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.tap.chatstory.R;
import com.tap.chatstory.data.homesource.model.ShareModel;
import com.tap.chatstory.data.homesource.model.UpdateModel;
import com.tap.chatstory.user.info.VIPActivity;


/**
 * Created by lebron on 2017/9/14.
 */

public class DialogUtils {

    public static void showShareDialog(final Activity activity, final ShareModel model) {
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
                ShareUtils.shareToFacebook(activity, null, model, null);
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.share_twitter_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareToTwitter(activity, model);
                alertDialog.dismiss();
            }
        });
    }

    public static void showWaittingDialog(final Activity activity, final OnWaittingDialogClickListener listener, boolean isAdLoaded, final CallbackManager callbackManager) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_need_pay, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    alertDialog.dismiss();
                    activity.finish();
                }
                return false;
            }
        });
        if (isAdLoaded) {
            dialogView.findViewById(R.id.watch_video_btn).setVisibility(View.VISIBLE);
            dialogView.findViewById(R.id.share_btn).setVisibility(View.GONE);
        } else {
            dialogView.findViewById(R.id.watch_video_btn).setVisibility(View.GONE);
            dialogView.findViewById(R.id.share_btn).setVisibility(View.VISIBLE);
        }
        dialogView.findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareModel shareModel = new ShareModel();
                shareModel.setContent(activity.getString(R.string.share_content));
                shareModel.setUrl(ShareUtils.getShareAppUrl(activity));
                ShareUtils.shareToFacebook(activity, callbackManager, shareModel, new ShareUtils.OnShareListener() {
                    @Override
                    public void onShare(boolean success) {
                        if (success) {
                            if (listener != null) {
                                listener.onOK();
                            }
                        } else {
                            if (listener != null) {
                                listener.onCancel();
                            }
                        }
                        alertDialog.dismiss();
                    }
                });
            }
        });
        dialogView.findViewById(R.id.pay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VIPActivity.start(activity);
                activity.finish();
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.watch_video_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.watch();
                }
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.pay_close_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onCancel();
                }
                alertDialog.dismiss();
            }
        });
        final TextView textView = dialogView.findViewById(R.id.waiting_count_txv);
        final CountDownTimer timer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                textView.setText(millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                if (listener != null) {
                    listener.onOK();
                }
                if (activity != null && !activity.isFinishing()) {
                    try {
                        alertDialog.dismiss();
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (timer != null) {
                    Log.i("Lebron", "timer cancel");
                    timer.cancel();
                }
            }
        });
        alertDialog.show();
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

    public static void showGetCoinsDialog(final Activity activity, String text) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_getcoins_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.NoBackGroundDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView textView = dialogView.findViewById(R.id.get_coins_txv);
        textView.setText(text);
        dialogView.findViewById(R.id.get_coins_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.pay_close_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void showLanguageDialog(final Activity activity, final OnLanguageChooseListener listener, String language) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        final String[] arrays = activity.getResources().getStringArray(R.array.language_array_tags);
        int index = -1;
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].equals(language)) {
                index = i;
                break;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.common_choose_language);
        builder.setSingleChoiceItems(R.array.language_array, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onChoose(arrays[which]);
                }
            }
        });
        builder.create().show();
    }

    public static void showGenderDialog(final Activity activity, int gender, final OnGenderChooseListener listener) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.common_choose_gender);
        builder.setSingleChoiceItems(R.array.gender_array, gender, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onChoose(which);
                }
            }
        });
        builder.create().show();
    }

    public static void showInputInviteCodeDialog(Activity activity, String code, final OnInviteCodeListener listener) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_input_invite_code, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        final EditText editText = dialogView.findViewById(R.id.waiting_count_txv);
        if (!TextUtils.isEmpty(code)) {
            editText.setText(code);
            editText.setEnabled(false);
            dialogView.findViewById(R.id.pay_btn).setEnabled(false);
        } else {
            dialogView.findViewById(R.id.pay_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        String code = editText.getText().toString();
                        if (!TextUtils.isEmpty(code)) {
                            listener.onCode(code);
                            alertDialog.dismiss();
                        }
                    }
                }
            });
        }
        dialogView.findViewById(R.id.pay_close_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public interface OnDialogClickListener {
        void onOK();

        void onCancel();
    }

    public interface OnWaittingDialogClickListener {
        void onOK();

        void onCancel();

        void watch();
    }

    public interface OnLanguageChooseListener {
        void onChoose(String language);
    }

    public interface OnGenderChooseListener {
        void onChoose(int gender);
    }

    public interface OnInviteCodeListener {
        void onCode(String code);
    }
}
