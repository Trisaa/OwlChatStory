/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.owl.chatstory.common.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.lang.ref.WeakReference;


/**
 * Log in button for logging into Twitter. When the button is clicked, an authorization request
 * is started and the user is presented with a screen requesting access to the user's Twitter
 * account. If successful, a {@link TwitterSession} is provided
 * in the {@link Callback#success(com.twitter.sdk.android.core.Result)}
 */
public class CustomTwitterLoginButton extends Button {

    final static String TAG = TwitterCore.TAG;
    static final String ERROR_MSG_NO_ACTIVITY = "TwitterLoginButton requires an activity."
            + " Override getActivity to provide the activity for this button.";

    final WeakReference<Activity> activityRef;
    volatile TwitterAuthClient authClient;
    Callback<TwitterSession> callback;

    public CustomTwitterLoginButton(Context context) {
        this(context, null);
    }

    public CustomTwitterLoginButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public CustomTwitterLoginButton(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, null);
    }

    CustomTwitterLoginButton(Context context, AttributeSet attrs, int defStyle,
                             TwitterAuthClient authClient) {
        super(context, attrs, defStyle);
        this.activityRef = new WeakReference<>(getActivity());
        this.authClient = authClient;
        //setupButton();

        checkTwitterCoreAndEnable();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupButton() {
        final Resources res = getResources();
//        super.setCompoundDrawablesWithIntrinsicBounds(
//                res.getDrawable(R.mipmap.tw__ic_logo_default), null, null, null);
//        super.setCompoundDrawablePadding(
//                res.getDimensionPixelSize(R.dimen.tw__login_btn_drawable_padding));
//        super.setText(R.string.tw__login_btn_txt);
//        super.setTextColor(res.getColor(R.color.tw__solid_white));
//        super.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//                res.getDimensionPixelSize(R.dimen.tw__login_btn_text_size));
//        super.setTypeface(Typeface.DEFAULT_BOLD);
//        super.setPadding(res.getDimensionPixelSize(R.dimen.tw__login_btn_left_padding), 0,
//                res.getDimensionPixelSize(R.dimen.tw__login_btn_right_padding), 0);
//        super.setBackgroundResource(R.mipmap.tw__login_btn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.setAllCaps(false);
        }
    }

    /**
     * @return the current {@link Callback}
     */
    public Callback<TwitterSession> getCallback() {
        return callback;
    }

    /**
     * Sets the {@link Callback} to invoke when login completes.
     *
     * @param callback The callback interface to invoke when login completes.
     * @throws IllegalArgumentException if callback is null.
     */
    public void setCallback(Callback<TwitterSession> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null");
        }
        this.callback = callback;
    }

    /**
     * Call this method when {@link Activity#onActivityResult(int, int, Intent)}
     * is called to complete the authorization flow.
     *
     * @param requestCode the request code used for SSO
     * @param resultCode  the result code returned by the SSO activity
     * @param data        the result data returned by the SSO activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == getTwitterAuthClient().getRequestCode()) {
            getTwitterAuthClient().onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Gets the activity. Override this method if this button was created with a non-Activity
     * context.
     */
    protected Activity getActivity() {
        if (getContext() instanceof Activity) {
            return (Activity) getContext();
        } else if (isInEditMode()) {
            return null;
        } else {
            throw new IllegalStateException(ERROR_MSG_NO_ACTIVITY);
        }
    }

    public void signIn() {
        checkCallback(callback);
        checkActivity(activityRef.get());

        getTwitterAuthClient().authorize(activityRef.get(), callback);
    }

    private void checkCallback(Callback callback) {
        if (callback == null) {

        }
    }

    private void checkActivity(Activity activity) {
        if (activity == null || activity.isFinishing()) {

        }
    }

    TwitterAuthClient getTwitterAuthClient() {
        if (authClient == null) {
            synchronized (CustomTwitterLoginButton.class) {
                if (authClient == null) {
                    authClient = new TwitterAuthClient();
                }
            }
        }
        return authClient;
    }

    private void checkTwitterCoreAndEnable() {
        //Default (Enabled) in edit mode
        if (isInEditMode()) return;

        try {
            TwitterCore.getInstance();
        } catch (IllegalStateException ex) {
            //Disable if TwitterCore hasn't started
            setEnabled(false);
        }
    }
}