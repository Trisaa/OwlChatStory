package com.tap.chatstory.data.chatsource.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lebron on 2018/3/9.
 */

public class ProgressModel implements Parcelable {
    public static final Parcelable.Creator<ProgressModel> CREATOR = new Parcelable.Creator<ProgressModel>() {
        @Override
        public ProgressModel createFromParcel(Parcel source) {
            return new ProgressModel(source);
        }

        @Override
        public ProgressModel[] newArray(int size) {
            return new ProgressModel[size];
        }
    };
    private String chapterId;
    private int progress;

    public ProgressModel() {
    }

    protected ProgressModel(Parcel in) {
        this.chapterId = in.readString();
        this.progress = in.readInt();
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chapterId);
        dest.writeInt(this.progress);
    }
}
