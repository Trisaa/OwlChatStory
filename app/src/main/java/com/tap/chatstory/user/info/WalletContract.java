package com.tap.chatstory.user.info;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.data.usersource.model.WalletModel;

/**
 * Created by lebron on 2018/4/16.
 */

public interface WalletContract {
    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {
        void showCoins(WalletModel model);
    }
}
