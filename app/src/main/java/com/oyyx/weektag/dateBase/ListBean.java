package com.oyyx.weektag.dateBase;

import org.litepal.crud.DataSupport;

/**
 * Created by 123 on 2017/9/15.
 */

public class ListBean extends DataSupport{


    private String title;
    private int month;
    private String img;
    private String year;
    private int day;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
