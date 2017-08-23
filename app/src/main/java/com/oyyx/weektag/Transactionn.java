package com.oyyx.weektag;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.util.UUID;

/**
 * Created by 123 on 2017/8/22.
 * 防止excSql报错
 *
 */

public class Transactionn extends DataSupport implements Parcelable {

    private String mUUID;

    private String title;

    private String memo;

    private long time;

    private int colour;

    private String mUri;

    public Transactionn(){
        mUUID = UUID.randomUUID().toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int color) {
        this.colour = color;
    }


    public String getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID.toString();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUUID);
        dest.writeString(this.title);
        dest.writeString(this.memo);
        dest.writeLong(this.time);
        dest.writeInt(this.colour);
        dest.writeString(this.mUri);
    }

    protected Transactionn(Parcel in) {
        this.mUUID = in.readString();
        this.title = in.readString();
        this.memo = in.readString();
        this.time = in.readLong();
        this.colour = in.readInt();
        this.mUri = in.readString();
    }

    public static final Parcelable.Creator<Transactionn> CREATOR = new Parcelable.Creator<Transactionn>() {
        @Override
        public Transactionn createFromParcel(Parcel source) {
            return new Transactionn(source);
        }

        @Override
        public Transactionn[] newArray(int size) {
            return new Transactionn[size];
        }
    };
}
