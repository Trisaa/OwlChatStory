package com.owl.chatstory.chat;

import android.text.TextUtils;
import android.util.Log;

import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.data.chatsource.ICollectData;
import com.owl.chatstory.data.chatsource.ICollectDataImpl;
import com.owl.chatstory.data.chatsource.IFictionData;
import com.owl.chatstory.data.chatsource.IFictionDataImpl;
import com.owl.chatstory.data.chatsource.IHistoryData;
import com.owl.chatstory.data.chatsource.IHistoryDataImpl;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.FictionStatusResponse;

/**
 * Created by lebron on 2017/9/14.
 */

public class ReadPresenter implements ReadContract.Presenter, IFictionData.OnFictionListener, ICollectData.FictionStatusListener {
    private ReadContract.View mView;
    private IFictionData mFictionData;
    private IHistoryData mHistoryData;
    private ICollectData mCollectData;

    public ReadPresenter(ReadContract.View view) {
        mView = view;
        mFictionData = new IFictionDataImpl();
        mHistoryData = new IHistoryDataImpl();
        mCollectData = new ICollectDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void getFictionData(String id) {
        mFictionData.getFictionDetail(id, "", this);
        mCollectData.isFictionCollected(id, this);
    }

    @Override
    public void getChapterData(String id, int vip, boolean allow) {
        Log.i("Lebron", " getChapterData " + vip + " " + allow);
        if (allow) {
            mFictionData.getChapterDetail(id, "", this);
        } else {
            //int random = new Random().nextInt(2);
            if (vip == 0) {
                mFictionData.getChapterDetail(id, "", this);
            } else {
                String skuID = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_PAID_FOR_VIP, null);
                if (!TextUtils.isEmpty(skuID)) {
                    mFictionData.getChapterDetail(id, "", this);
                } else {
                    mView.showWaittingDialog(id);
                }
            }
        }
    }

    @Override
    public void addToHistory(String id) {
        mHistoryData.addToHistory(id);
    }

    @Override
    public void collectFiction(String id) {
        mCollectData.collectFiction(id);
    }

    @Override
    public void uncollectFiction(String id) {
        mCollectData.uncollectFiction(id);
    }

    @Override
    public void prayUpdate(String id) {
        mCollectData.prayUpdate(id);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mFictionData.clearSubscriptions();
    }

    @Override
    public void onFiction(FictionModel model) {
        mView.showFictionData(model);
    }

    @Override
    public void onFictionDetail(FictionDetailModel model) {
        if (model != null && model.getChapters() != null) {
            mView.showFictionDetailData(model);
            getChapterData(model.getChapters().get(0).getChapterId(), model.getChapters().get(0).getVip(), false);
        }
    }

    @Override
    public void onStatus(FictionStatusResponse response) {
        mView.updateFictionStatus(response);
    }
}
