package com.owl.chatstory;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.util.network.HttpUtils;
import com.owl.chatstory.data.usersource.IUserDataImpl;

import rx.Subscriber;

/**
 * Created by lebron on 2018/3/1.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Lebron", "Refreshed token: " + refreshedToken);
        try {
            HttpUtils.getInstance().uploadDeviceToken(new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.i("Lebron", "MyFirebaseInstanceIDService upload device failed "+e.toString());
                    PreferencesHelper.getInstance().setBoolean(PreferencesHelper.KEY_DEVICE_TOKEN_UPLOADED, false);
                }

                @Override
                public void onNext(Object o) {
                    Log.i("Lebron", "MyFirebaseInstanceIDService upload device success");
                    PreferencesHelper.getInstance().setBoolean(PreferencesHelper.KEY_DEVICE_TOKEN_UPLOADED, true);
                }
            }, refreshedToken);
        } catch (Exception e) {
            Log.d("Lebron", "Refreshed token: " + e.toString());
        }
    }
}
