package com.tap.chatstory.data.usersource;

import android.content.Intent;
import android.util.Log;

import com.tap.chatstory.common.view.CustomTwitterLoginButton;
import com.tap.chatstory.data.usersource.model.UserModel;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;


/**
 * Created by kince on 2016/4/5.
 */
public class TwitterAuth extends SignInAuth {
    private TwitterSession mTwitterSession;
    private CustomTwitterLoginButton mCustomTwitterLoginButton;

    private Callback<TwitterSession> mCallbackTwitterSession = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
            TwitterSession session = result.data;
            mTwitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();

            final long userId = mTwitterSession.getUserId();
            final String username = mTwitterSession.getUserName();
            Log.i("TwitterAuth", " success  " + username + userId);
            TwitterCore.getInstance().getApiClient(session).getAccountService()
                    .verifyCredentials(true, false, true).enqueue(new Callback<User>() {
                @Override
                public void failure(TwitterException e) {
                    Log.i("TwitterAuth", e.getMessage());
                    onAuthListener.onAuthError(e.getMessage());
                }

                @Override
                public void success(Result<User> userResult) {
                    User user = userResult.data;
                    Log.i("TwitterAuth", user.profileImageUrl.replace("_normal", ""));
                    String id = user.idStr;
                    String name = user.name;
                    int gender = 1;
                    String location = user.location;
                    String profileImage = user.profileImageUrl.replace("_normal", "");
                    String des = user.description;

                    UserModel userModel = new UserModel();
                    userModel.setName(name);
                    userModel.setPlatform("twitter");
                    userModel.setPlatformId(id);
                    userModel.setIcon(profileImage);
                    onAuthListener.onAuthSuccess(userModel);
                }
            });

        }

        @Override
        public void failure(TwitterException exception) {
            Log.i("TwitterAuth", " failture  " + exception.toString());
            onAuthListener.onAuthError(exception.getMessage());
        }
    };

    public TwitterAuth(CustomTwitterLoginButton customTwitterLoginButton) {
        mCustomTwitterLoginButton = customTwitterLoginButton;
        mCustomTwitterLoginButton.setCallback(mCallbackTwitterSession);
    }

    @Override
    public void signIn() {
        mCustomTwitterLoginButton.signIn();
    }

    @Override
    public void signOut() {

    }

    @Override
    public void revoke() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            TwitterCore.getInstance().getSessionManager().clearActiveSession();
        }
        onAuthListener.onRevoke();
    }


    @Override
    public void release() {
    }

    public void resultLogin(int requestCode, int resultCode, Intent data) {
        if (mCustomTwitterLoginButton != null) {
            mCustomTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }

}

