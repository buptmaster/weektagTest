package com.oyyx.weektag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 123 on 2017/8/22.
 */

public class CalendarUtils {

    public static long[] getTime(long time) {
        long different = time - (new Date()).getTime();
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

        return new long[]{elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds};
    }

    public static long timeToDate(int year,int month,int day,int hour,int min) {
        String monthstr = month + "";
        String daystr = day + "";
        String hourstr = hour + "";
        String minstr = min + "";
        if (month<10)
            monthstr = ("0" + monthstr);
        if (day<10)
            daystr = "0"+daystr;
        if (hour<10)
            hourstr = "0"+hourstr;
        if (min<10)
            minstr = "0" + minstr;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = ""+year+"-"+monthstr+"-"+daystr+" "+hourstr+":"+minstr;
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

}
