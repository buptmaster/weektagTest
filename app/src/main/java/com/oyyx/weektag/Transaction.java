package com.oyyx.weektag;

import android.net.Uri;

import org.litepal.crud.DataSupport;

/**
 * Created by 123 on 2017/8/22.
 */

public class Transaction extends DataSupport {

    private String title;

    private String memo;

    private int colour;

    private int year;

    private int month;

    private int day;

    private int hour;

    private int min;

    private Uri mUri;

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }
}
