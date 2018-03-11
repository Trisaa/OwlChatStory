package com.tap.chatstory;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by lebron on 2017/9/4.
 */

public class MainApplication extends Application {
    private static Context context;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initTwitter();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Fabric.with(this, new Crashlytics());
    }

    private void initTwitter() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("r8Of5bAlbCoDnrMc7j60JEnFP", "1ON6sa9f2CZx92OjWBn9g2o5RzCpyZ3kwmNjsO4kfWHpHwVbU3"))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }
}
