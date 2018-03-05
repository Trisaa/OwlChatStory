package com.owl.chatstory.user.info;

import com.owl.chatstory.data.usersource.IMessageData;
import com.owl.chatstory.data.usersource.IMessageDataImpl;
import com.owl.chatstory.data.usersource.IUserData;
import com.owl.chatstory.data.usersource.IUserDataImpl;
import com.owl.chatstory.data.usersource.model.MessagesModel;
import com.owl.chatstory.data.usersource.model.UserModel;

import java.util.List;

/**
 * Created by lebron on 2018/1/30.
 */

public class UserPresenter implements UserContract.Presenter, IUserData.OnUserInfoListener, IMessageData.OnMessageListListener {
    private UserContract.View mView;
    private IUserData mData;
    private IMessageData mMessageData;

    public UserPresenter(UserContract.View view) {
        mView = view;
        mData = new IUserDataImpl();
        mMessageData = new IMessageDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mData.getUserInfo(this);
        mMessageData.getUnreadCount(this);
    }

    @Override
    public void unsubscribe() {
        mData.clearSubscriptions();
    }

    @Override
    public void onUserInfo(UserModel model) {
        mView.showUserInfo(model);
    }

    @Override
    public void uploadDeviceToken(String token) {
        mData.uploadDeviceToken(token);
    }

    @Override
    public void getUnreadCount() {
        mMessageData.getUnreadCount(this);
    }

    @Override
    public void onMessageList(List<MessagesModel> list) {

    }

    @Override
    public void onMessageCount(int count) {
        mView.showMessageCount(count);
    }
}
