package com.oyyx.weektag;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class WeekTagWidget extends AppWidgetProvider {

    private static final String TAG = "WeekTagWidget";

    private Intent WIDGET_SERVICE = new Intent("android.appwidget.action.WIDGET_SERVICE");

    private final String UPDATE_TIME = "android.appwidget.action.APPWIDGET_DAY";

    private final String UPDATE = "android.appwidget.action.APPWIDGET_UPDATE_SERVICE";

    private static Set idSet = new HashSet();

    private static Transactionn mTransaction;

    private static long days;

    public void updateALLAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Set set) {

        int appWidgetId;

        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {

            appWidgetId = ((Integer) iterator.next()).intValue();

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.week_tag_widget);
            views.setTextViewText(R.id.widget_title, mTransaction.getTitle());
            long times[] = CalendarUtils.getTime(mTransaction.getTime());
            days = times[0];
            views.setTextViewText(R.id.widget_time, days + "");
            views.setImageViewResource(R.id.widget_bg,R.drawable.background_2);
            views.setOnClickPendingIntent(R.id.widget_click,getPendingIntent(context));
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            idSet.add(appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        //显式启动service
        WIDGET_SERVICE.setPackage("com.oyyx.weektag");
        context.startService(WIDGET_SERVICE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(UPDATE_TIME);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 24 * 3600 * 1000, AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG,"Widget On Receive");
        super.onReceive(context, intent);
        final String action = intent.getAction();
        Log.e(TAG, action);

        if (action.equals(UPDATE)) {
            mTransaction = intent.getParcelableExtra("transaction");
            updateALLAppWidget(context,AppWidgetManager.getInstance(context),idSet);
        }else if (action.equals(UPDATE_TIME)){
            days--;
            updateALLAppWidget(context, AppWidgetManager.getInstance(context),idSet);
        }
    }

    private PendingIntent getPendingIntent(Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent("android.appwidget.action.REQUEST_UPDATE"), 0);

    }
}

