package com.tap.chatstory;

import com.tap.chatstory.common.util.DeviceUtils;
import com.tap.chatstory.data.homesource.IUpdateData;
import com.tap.chatstory.data.homesource.IUpdateDataImpl;
import com.tap.chatstory.data.homesource.model.UpdateModel;

/**
 * Created by lebron on 2017/9/22.
 */

public class MainPresenter implements MainContract.Presenter, IUpdateData.OnUpdateListener {
    private MainContract.View mView;
    private IUpdateData mUpdateData;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mUpdateData = new IUpdateDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mUpdateData.checkUpdateState(this);
    }

    @Override
    public void unsubscribe() {
        mUpdateData.clearSubscription();
    }

    @Override
    public void onUpdate(UpdateModel model) {
        if (model.getLatestVersion() > DeviceUtils.getVersionCode(MainApplication.getAppContext())) {
            mView.showUpdateDialog(model);
        }
    }
}
