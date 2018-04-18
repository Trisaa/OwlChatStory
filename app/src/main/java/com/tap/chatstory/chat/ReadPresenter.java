package com.tap.chatstory.chat;

import android.text.TextUtils;
import android.util.Log;

import com.tap.chatstory.common.util.PreferencesHelper;
import com.tap.chatstory.data.chatsource.ICollectData;
import com.tap.chatstory.data.chatsource.ICollectDataImpl;
import com.tap.chatstory.data.chatsource.IFictionData;
import com.tap.chatstory.data.chatsource.IFictionDataImpl;
import com.tap.chatstory.data.chatsource.IHistoryData;
import com.tap.chatstory.data.chatsource.IHistoryDataImpl;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;
import com.tap.chatstory.data.chatsource.model.FictionModel;
import com.tap.chatstory.data.usersource.ICoinData;
import com.tap.chatstory.data.usersource.ICoinDataImpl;

/**
 * Created by lebron on 2017/9/14.
 */

public class ReadPresenter implements ReadContract.Presenter, IFictionData.OnFictionListener, ICoinData.OnRewardedListener {
    private ReadContract.View mView;
    private IFictionData mFictionData;
    private IHistoryData mHistoryData;
    private ICollectData mCollectData;
    private ICoinData mCoinData;

    public ReadPresenter(ReadContract.View view) {
        mView = view;
        mFictionData = new IFictionDataImpl();
        mHistoryData = new IHistoryDataImpl();
        mCollectData = new ICollectDataImpl();
        mCoinData = new ICoinDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void getChapterData(String id, int vip, boolean allow) {
        Log.i("Lebron", " getChapterData " + vip + " " + allow);
        if (allow) {
            mFictionData.getChapterDetail(id, "", this);
        } else {
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
    public void likeFiction(int status, String id) {
        mCollectData.likeFiction(status, id);
    }

    @Override
    public void prayUpdate(String id) {
        mCollectData.prayUpdate(id);
    }

    @Override
    public void getRewards(String source) {
        mCoinData.getRewards(source, this);
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
    }

    @Override
    public void onRewarded() {
        mView.showRewards();
    }
}
