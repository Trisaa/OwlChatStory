package com.tap.chatstory.chat.adapter;

import android.view.View;
import android.widget.Toast;

import com.tap.chatstory.MainApplication;
import com.tap.chatstory.R;
import com.tap.chatstory.data.chatsource.model.ChapterModel;
import com.tap.chatstory.data.chatsource.model.MessageModel;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lebron on 2017/9/13.
 */

public class ChatNextDelegate implements ItemViewDelegate<MessageModel> {
    private OnClickListener mListener;

    public ChatNextDelegate(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.read_next_item;
    }

    @Override
    public boolean isForViewType(MessageModel item, int position) {
        return item.getLocation().equals("end");
    }

    @Override
    public void convert(ViewHolder holder, final MessageModel messageModel, int position) {
        holder.setVisible(R.id.read_over_layout, messageModel.isEnded());
        holder.setVisible(R.id.read_next_chapter_layout, false);
        if (!messageModel.isLastChapter() && messageModel.getNextChapterModel() != null) {
            holder.setText(R.id.read_next_fiction_name_txv, messageModel.getFictionName());
            holder.setText(R.id.read_next_chapter_name_txv, MainApplication.getAppContext().getString(R.string.chapter_num,
                    messageModel.getNextChapterModel().getNum(), messageModel.getNextChapterModel().getChapterName()));
            holder.setVisible(R.id.read_next_chapter_layout, true);
        }
        //holder.setText(R.id.read_over_txv, messageModel.isLastChapter() ? MainApplication.getAppContext().getString(R.string.common_over) : MainApplication.getAppContext().getString(R.string.common_continue));
        if (messageModel.isLastChapter()) {
            holder.setText(R.id.read_share_txv, MainApplication.getAppContext().getString(R.string.read_please_update));
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick();
                }
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onLongClick();
                }
                return false;
            }
        });
        holder.setOnClickListener(R.id.read_share_txv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageModel.isLastChapter()) {
                    if (mListener != null) {
                        mListener.onShareClick(false);
                    }
                    Toast.makeText(MainApplication.getAppContext(), R.string.read_update_feedback, Toast.LENGTH_SHORT).show();
                } else {
                    if (mListener != null) {
                        mListener.onShareClick(true);
                    }
                }
            }
        });
        holder.setOnClickListener(R.id.read_next_txv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onNextClick(messageModel.getNextChapterModel());
                }
            }
        });
    }

    public interface OnClickListener {
        void onClick();

        void onShareClick(boolean shareOrUpdate);

        void onLongClick();

        void onNextClick(ChapterModel chapterModel);
    }
}
