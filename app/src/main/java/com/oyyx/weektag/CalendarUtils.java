package com.oyyx.weektag;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by 123 on 2017/8/22.
 * 一个处理日程事件及日期转换的工具类
 */

public class CalendarUtils {

    private static String CALANDER_URL = "content://com.android.calendar/calendars";
    private static String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";
    private static String CALENDARS_NAME = "test";
    private static String CALENDARS_ACCOUNT_NAME = "test@gmail.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.exchange";
    private static String CALENDARS_DISPLAY_NAME = "测试账户";

    private static long ONE_HOUR = 60 * 60 * 1000;

    //将毫秒数转换称天数，小时数，分钟数，秒数
    public static long[] getTime(long time) {
        long different = time - System.currentTimeMillis();

        Log.e("342234235", "getTime: "+System.currentTimeMillis());
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        Log.e("_____", elapsedDays + " " + elapsedHours + " " + elapsedMinutes);

        if(elapsedDays<0||elapsedHours<0||elapsedMinutes<0||elapsedSeconds<0){
            return new long[]{0,0,0,0};
        }

        return new long[]{elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds};
    }

    //将一个特定日期转换成毫秒数
    public static long timeToDate(int year, int month, int day, int hour, int min) {

        Log.e("erwer", month+"");

        String monthstr = month + "";
        String daystr = day + "";
        String hourstr = hour + "";
        String minstr = min + "";

        if (month < 10)
            monthstr = ("0" + monthstr);
        if (day < 10)
            daystr = "0" + daystr;
        if (hour < 10)
            hourstr = "0" + hourstr;
        if (min < 10)
            minstr = "0" + minstr;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = "" + year + "-" + monthstr + "-" + daystr + " " + hourstr + ":" + minstr;
        Date date = null;
        try {
            date = format.parse(str);
            Log.e("--------->date", date.getTime()+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("e", "timeToDate: "+date.toString());
        return date.getTime();
    }

    //检查calendar是否注册了账户
    public static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALANDER_URL), null, null, null, null);
        try {
            if (userCursor == null)//查询返回空值
                return -1;
            int count = userCursor.getCount();
            if (count > 0) {//存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    //添加测试账户
    public static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALANDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    //添加日程
    public static void addCalendarEvent(Context context,String title, String description, long beginTime){
        // 获取日历账户的id
        int calId = checkAndAddCalendarAccount(context);
        if (calId < 0) {
            // 获取账户id失败直接返回，添加日历事件失败
            return;
        }

        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", description);
        // 插入账户的id
        event.put("calendar_id", calId);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(beginTime);//设置开始时间
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(start + ONE_HOUR);//设置终止时间
        long end = mCalendar.getTime().getTime();

        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");  //这个是时区，必须有，
        //添加事件
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALANDER_EVENT_URL), event);
        if (newEvent == null) {
            // 添加日历事件失败直接返回
            return;
        }
        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        // 提前10分钟有提醒
        values.put(CalendarContract.Reminders.MINUTES, 10);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALANDER_REMIDER_URL), values);
        if(uri == null) {
            // 添加闹钟提醒失败直接返回
            return;
        }
    }

    //检查并添加账户
    private static int checkAndAddCalendarAccount(Context context){
        int oldId = checkCalendarAccount(context);
        if( oldId >= 0 ){
            return oldId;
        }else{
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    //获取日历中的日程
    public static List<Transactionn> getCalendarEvent(Context context,List<Transactionn> transactionns) {
        Uri uri = Uri.parse(CALANDER_EVENT_URL);
        int flag = 0;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            Transactionn transactionn = new Transactionn();
            if(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART))<(new Date()).getTime()){
                continue;
            }
            if(transactionns.size() != 0) {
                for (Transactionn transactionn1 : transactionns) {
                    if(transactionn1.getTime() == cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART))){
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0){
                    transactionn.setTime(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART)));
                    transactionn.setTitle(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE)));
                    transactionn.setColour(Color.BLUE);
                    transactionns.add(transactionn);
                    transactionn.save();
                }
            }else {
                transactionn.setTime(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART)));
                transactionn.setTitle(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE)));
                transactionn.setColour(Color.BLUE);
                transactionns.add(transactionn);
                transactionn.save();
            }
        }
        cursor.close();
        return transactionns;
    }


}
