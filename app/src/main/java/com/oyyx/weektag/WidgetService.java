package com.oyyx.weektag;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import org.litepal.LitePal;

import java.util.List;

public class WidgetService extends Service {

    private List<Transactionn> mTransactionns;

    private Context mContext;

    private static int position = 0;

    private BroadcastReceiver mBroadcastReceiver;

    public WidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("-------->","ServiceCreated");
        mContext = getApplicationContext();
        mTransactionns = TransactionLab.get().getTransactionns();
        LitePal.initialize(this);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mTransactionns.size() !=0)
                    sendUpdate();
            }
        };

        registerReceiver(mBroadcastReceiver,new IntentFilter("android.appwidget.action.REQUEST_UPDATE"));
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("-------->","ServiceStartCommand");
        if (mTransactionns.size() !=0)
            sendUpdate();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void sendUpdate() {
        mTransactionns = TransactionLab.get().getTransactionns();
        Intent updateIntent = new Intent("android.appwidget.action.APPWIDGET_UPDATE_SERVICE");
        updateIntent.putExtra("transaction", mTransactionns.get(position));
        mContext.sendBroadcast(updateIntent);
        position++;
        if(position == mTransactionns.size()){
            position = 0;
        }
    }
}
