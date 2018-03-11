package com.tap.chatstory.data.usersource;

import com.tap.chatstory.data.usersource.model.MessagesModel;

import java.util.List;

/**
 * Created by lebron on 2018/3/2.
 */

public interface IMessageData {
    void clearSubscriptions();

    void getMessageList(int page, OnMessageListListener listener);

    void getUnreadCount(OnMessageListListener listener);

    void readMessage(String id);

    interface OnMessageListListener {
        void onMessageList(List<MessagesModel> list);

        void onMessageCount(int count);
    }
}
