package com.oyyx.weektag;

import android.net.Uri;

import org.litepal.crud.DataSupport;

import java.util.UUID;

/**
 * Created by 123 on 2017/8/22.
 * 防止excSql报错
 *
 */

public class Transactionn extends DataSupport {

    private String mUUID;

    private String title;

    private String memo;

    private long time;

    private int colour;

    private Uri mUri;

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

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
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
}
