package com.tap.chatstory.user.info;

import com.tap.chatstory.data.usersource.ICoinData;
import com.tap.chatstory.data.usersource.ICoinDataImpl;
import com.tap.chatstory.data.usersource.model.WalletModel;

/**
 * Created by lebron on 2018/4/16.
 */

public class WalletPresenter implements WalletContract.Presenter, ICoinData.OnUserCoinListener {
    private WalletContract.View mView;
    private ICoinData mData;

    public WalletPresenter(WalletContract.View view) {
        mView = view;
        mData = new ICoinDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mData.getUserCoins(this);
    }

    @Override
    public void unsubscribe() {
        mData.clearSubscriptions();
    }

    @Override
    public void onCoin(WalletModel model) {
        mView.showCoins(model);
    }
}
