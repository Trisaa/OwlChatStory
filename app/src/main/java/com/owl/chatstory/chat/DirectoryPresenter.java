package com.owl.chatstory.chat;

import com.owl.chatstory.data.chatsource.ICollectData;
import com.owl.chatstory.data.chatsource.ICollectDataImpl;
import com.owl.chatstory.data.chatsource.IFictionData;
import com.owl.chatstory.data.chatsource.IFictionDataImpl;
import com.owl.chatstory.data.chatsource.model.ChapterModel;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.FictionStatusResponse;

import java.util.List;

/**
 * Created by lebron on 2017/10/30.
 */

public class DirectoryPresenter implements DirectoryContract.Presenter, IFictionData.OnChapterListener, IFictionData.OnFictionListener, ICollectData.FictionStatusListener {
    private DirectoryContract.View mView;
    private ICollectData mData;
    private IFictionData mFictionData;

    public DirectoryPresenter(DirectoryContract.View view) {
        mView = view;
        mData = new ICollectDataImpl();
        mFictionData = new IFictionDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mData.clearSubscriptions();
    }

    @Override
    public void collectFiction(String operation, String id) {
        if (operation.equals(DirectoryContract.COLLECT_FICTION)) {
            mData.collectFiction(id);
        } else {
            mData.uncollectFiction(id);
        }
    }

    @Override
    public void likeFiction(int status, String id) {
        mData.likeFiction(status, id);
    }

    @Override
    public void getChapterList(String id, int page) {
        mFictionData.getChapterList(id, page, this);
    }

    @Override
    public void getFictionDetail(String id) {
        mFictionData.getFictionDetail(id, "", this);
        mData.isFictionCollected(id, this);
    }

    @Override
    public void onChapterList(List<ChapterModel> list) {
        mView.showChapterList(list);
    }

    @Override
    public void onFiction(FictionModel model) {

    }

    @Override
    public void onFictionDetail(FictionDetailModel model) {
        mView.showFictionDetail(model);
    }

    @Override
    public void onStatus(FictionStatusResponse response) {
        mView.showFictionStatus(response);
    }
}
