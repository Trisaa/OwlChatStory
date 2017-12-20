package com.owl.chatstory.chat;

import com.owl.chatstory.data.chatsource.ICollectData;
import com.owl.chatstory.data.chatsource.ICollectDataImpl;
import com.owl.chatstory.data.chatsource.IFictionData;
import com.owl.chatstory.data.chatsource.IFictionDataImpl;
import com.owl.chatstory.data.chatsource.IHistoryData;
import com.owl.chatstory.data.chatsource.IHistoryDataImpl;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;

/**
 * Created by lebron on 2017/9/14.
 */

public class ReadPresenter implements ReadContract.Presenter, IFictionData.OnFictionListener, ICollectData.CollectListener {
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
    public void getChapterData(String id) {
        mFictionData.getChapterDetail(id, "", this);
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
            mFictionData.getChapterDetail(model.getChapters().get(0).getChapterId(), "", this);
        }
    }

    @Override
    public void isCollect(boolean collected) {
        mView.updateCollectState(collected);
    }
}
