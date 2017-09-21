package com.oyyx.weektag.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.IBinder;

import com.oyyx.weektag.activity.MainActivity;
import com.oyyx.weektag.R;
import com.oyyx.weektag.dateBase.TransactionLab;
import com.oyyx.weektag.dateBase.Transactionn;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

/**
 * widget 服务和放松事件完成通知服务
 */
public class WidgetService extends Service {



    //事件列表
    private List<Transactionn> mTransactionns;

    //上下文对象
    private Context mContext;

    //事件位置
    private static int position = 0;

    //广播接收器
    private BroadcastReceiver mBroadcastReceiver;


    public WidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        LitePal.initialize(this);
        mTransactionns = TransactionLab.get().getTransactionnsByDefault();


        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.appwidget.action.REQUEST_UPDATE"))
                    sendUpdate();
                else if(intent.getAction().equals("closeForegroundService")){
                    stopForeground(true);
                }
            }
        };

        //设置广播过滤器
        IntentFilter intentFilter = new IntentFilter();
        //更新widget
        intentFilter.addAction("android.appwidget.action.REQUEST_UPDATE");
        //关闭前台服务
        intentFilter.addAction("closeForegroundService");
        registerReceiver(mBroadcastReceiver,intentFilter);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sendUpdate();

        mTransactionns = TransactionLab.get().getTransactionnsByDefault();
        int id = 1;
        for(Transactionn transactionn : mTransactionns){
            //提前提醒
            if (new Date().getTime() - transactionn.getTime()>=3600000){
                sendCompleteNotice(transactionn,id);
                id++;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void sendUpdate() {
        mTransactionns = TransactionLab.get().getTransactionnsByDefault();
        if(mTransactionns.size()!=0) {
            Intent updateIntent = new Intent("android.appwidget.action.APPWIDGET_UPDATE_SERVICE");
            updateIntent.putExtra("transaction", mTransactionns.get(position));
            mContext.sendBroadcast(updateIntent);
            position++;
            if (position == mTransactionns.size()) {
                position = 0;
            }
        }
    }

    /**
     * 发送任务完成通知
     * @param transactionn
     * @param id
     */
    private void sendCompleteNotice(Transactionn transactionn,int id){
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("事件已完成")
                .setContentText(transactionn.getTitle() + " 已完成，请前往删除事件")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon_tag)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_tag))
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(id, notification);
    }

}
