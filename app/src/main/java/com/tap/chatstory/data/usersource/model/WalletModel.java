package com.tap.chatstory.data.usersource.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2018/4/16.
 */

public class WalletModel implements Parcelable {
    public static final Parcelable.Creator<WalletModel> CREATOR = new Parcelable.Creator<WalletModel>() {
        @Override
        public WalletModel createFromParcel(Parcel source) {
            return new WalletModel(source);
        }

        @Override
        public WalletModel[] newArray(int size) {
            return new WalletModel[size];
        }
    };
    @SerializedName("mcoin")
    private int coin;

    public WalletModel() {
    }

    protected WalletModel(Parcel in) {
        this.coin = in.readInt();
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.coin);
    }
}
