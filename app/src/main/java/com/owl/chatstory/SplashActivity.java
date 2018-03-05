package com.owl.chatstory;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.Constants;

import butterknife.BindView;

import static com.owl.chatstory.MainActivity.EXTRA_FICTION_ID;
import static com.owl.chatstory.MainActivity.EXTRA_FROM_NOTIFICATION;

/**
 * Created by lebron on 2017/9/15.
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.splash_des_txv)
    TextView mTextView;

    private int from = -1;
    private String mFictionId;

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

        String type = getIntent().getStringExtra("type");
        String fictionId = getIntent().getStringExtra("ifiction_id");
        Log.i("Lebron", "the key is " + type + " " + fictionId);
        if (type != null && fictionId != null) {
            from = Integer.valueOf(type);
            mFictionId = fictionId;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra(EXTRA_FROM_NOTIFICATION, from);
                intent.putExtra(EXTRA_FICTION_ID, mFictionId);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
