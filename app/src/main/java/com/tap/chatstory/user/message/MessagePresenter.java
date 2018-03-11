package com.tap.chatstory.user.message;

import com.tap.chatstory.data.usersource.IMessageData;
import com.tap.chatstory.data.usersource.IMessageDataImpl;
import com.tap.chatstory.data.usersource.model.MessagesModel;

import java.util.List;

/**
 * Created by lebron on 2018/3/2.
 */

public class MessagePresenter implements MessageContract.Presenter, IMessageData.OnMessageListListener {
    private MessageContract.View mView;
    private IMessageData mData;

    public MessagePresenter(MessageContract.View view) {
        mView = view;
        mData = new IMessageDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void getUnreadMessageCount() {
        mData.getUnreadCount(this);
    }

    @Override
    public void readMessage(String id) {
        mData.readMessage(id);
    }

    @Override
    public void getMessageList(int page) {
        mData.getMessageList(page, this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onMessageList(List<MessagesModel> list) {
        mView.showMessageList(list);
    }

    @Override
    public void onMessageCount(int count) {
        mView.showMessageCount(count);
    }
}
