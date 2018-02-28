package com.owl.chatstory.data.chatsource.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/12/20.
 */

public class FictionStatusResponse implements Parcelable {
    public static final Parcelable.Creator<FictionStatusResponse> CREATOR = new Parcelable.Creator<FictionStatusResponse>() {
        @Override
        public FictionStatusResponse createFromParcel(Parcel source) {
            return new FictionStatusResponse(source);
        }

        @Override
        public FictionStatusResponse[] newArray(int size) {
            return new FictionStatusResponse[size];
        }
    };
    @SerializedName("collect")
    private int collect;
    @SerializedName("thumbsup")
    private int liked;

    public FictionStatusResponse() {
    }

    protected FictionStatusResponse(Parcel in) {
        this.collect = in.readInt();
        this.liked = in.readInt();
    }

    public boolean getCollect() {
        return collect == 1 ? true : false;
    }

    public void setCollect(boolean collect) {
        this.collect = collect ? 1 : 0;
    }

    public boolean getLiked() {
        return liked == 1 ? true : false;
    }

    public void setLiked(boolean liked) {
        this.liked = liked ? 1 : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.collect);
        dest.writeInt(this.liked);
    }
}
