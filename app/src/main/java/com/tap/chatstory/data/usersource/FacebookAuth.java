package com.tap.chatstory.data.usersource;

/**
 * Created by lebron on 16-11-7.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.tap.chatstory.data.usersource.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kince on 2016/4/5.
 */

public class FacebookAuth extends SignInAuth {
    FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.i("FacebookAuth", loginResult.toString());
            AccessToken fbAccessToken = loginResult.getAccessToken();
            if (fbAccessToken != null) {
                loginFBProfileOfToken(fbAccessToken);
            } else {
                // AccessToken 过期重新登录
                onAuthListener.onAuthCancel();
            }
        }

        @Override
        public void onCancel() {
            if (onAuthListener != null) {
                onAuthListener.onAuthCancel();
            } else {
                throw new IllegalArgumentException("interface InfoLoginFaceCallback is null");
            }
        }

        @Override
        public void onError(FacebookException error) {
            if (onAuthListener != null) {
                Log.i("FacebookAuth", error.getMessage());
                onAuthListener.onAuthError(error.getMessage());
            } else {
                throw new IllegalArgumentException("interface InfoLoginFaceCallback is null");
            }
        }
    };
    private FragmentActivity mActivity;
    private CallbackManager mCallbackManager;
    private LoginManager mLoginManager;

    public FacebookAuth(FragmentActivity mActivity) {
        this.mLoginManager = LoginManager.getInstance();
        this.mCallbackManager = CallbackManager.Factory.create();
        this.mLoginManager.registerCallback(mCallbackManager, mCallback);
        this.mActivity = mActivity;
    }

    /**
     * @param fbAccessToken
     */

    private void loginFBProfileOfToken(AccessToken fbAccessToken) {
        GraphRequest meRequest = GraphRequest.newMeRequest(
                fbAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        if (response.getError() == null) {
                            Log.i("Lebron",object.toString());
                            String id = null;
                            try {
                                id = object.getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String name;
                            try {
                                name = object.getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                name = "";
                            }

                            int gender = 1;
                            if ("male".equals(object.optString("gender"))) {
                                gender = 1;
                            } else if ("female".equals(object.optString("gender"))) {
                                gender = 0;
                            }

                            String location = null;
                            try {
                                if (object.has("location")) {
                                    location = object.getJSONObject("location").optString("name");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                location = "";
                            }

                            String avatarUrl = null;
                            try {
                                if (object.has("picture")) {
                                    avatarUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                avatarUrl = "";
                            }

                            UserModel userModel = new UserModel();
                            userModel.setPlatformId(id);
                            userModel.setName(name);
                            userModel.setIcon(avatarUrl);
                            userModel.setPlatform("facebook");

                            if (onAuthListener != null) {
                                onAuthListener.onAuthSuccess(userModel);
                            } else {
                                throw new IllegalArgumentException("interface InfoLoginFaceCallback is null");
                            }
                        }
                    }
                });

        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,email,gender,picture.type(square)");
        parameters.putString("fields", "id,name,email,gender,picture.width(608).height(608)");
        meRequest.setParameters(parameters);
        meRequest.executeAsync();

    }

    /**
     * 从Facebook授权登录
     */

    public void signIn() {
        List<String> permissionNeeds = Arrays.asList("public_profile,email,user_friends");
        AccessToken fbAccessToken = AccessToken.getCurrentAccessToken();
        if (fbAccessToken != null && !fbAccessToken.isExpired()) {
            loginFBProfileOfToken(fbAccessToken);
        } else {
            mLoginManager.logInWithReadPermissions(mActivity, permissionNeeds);
        }
    }

    @Override
    public void signOut() {
        mLoginManager.logOut();
    }

    @Override
    public void revoke() {
        AccessToken token = AccessToken.getCurrentAccessToken();

        if (token != null) {
            String user = token.getUserId();
            //url to revoke login DELETE {user_id}/permissions2
            String graphPath = user + "/permissions";

            final GraphRequest requestLogout = new GraphRequest(token, graphPath, null, HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    //call logout to clear token info and profile
                    signOut();
                    onAuthListener.onRevoke();
                }
            });

            requestLogout.executeAsync();
        } else {
            onAuthListener.onAuthError("Token is null");
        }
    }

    @Override
    public void release() {
        mActivity = null;
    }

    public void resultLogin(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
