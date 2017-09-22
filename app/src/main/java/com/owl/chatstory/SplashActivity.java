package com.owl.chatstory;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.TextView;

import com.owl.chatstory.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by lebron on 2017/9/15.
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.splash_des_txv)
    TextView mTextView;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initViewsAndData() {
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "splash.ttf");
        mTextView.setTypeface(tf);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
