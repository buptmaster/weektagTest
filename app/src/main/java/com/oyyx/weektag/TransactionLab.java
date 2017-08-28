package com.oyyx.weektag;


import android.content.Context;
import android.os.Environment;
import android.text.method.TransformationMethod;
import android.util.Log;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by 123 on 2017/8/22.
 * 一个持久化层
 */

public class TransactionLab {

    private static TransactionLab sTransaction;

    private List<Transactionn> mTransactionns;

    //单例
    public static TransactionLab get() {
        if (sTransaction == null) {
            sTransaction = new TransactionLab();
        }
        return sTransaction;
    }

    private TransactionLab() {

    }

    public List<Transactionn> getTransactionns(){
        return mTransactionns;
    }


    //得到所有的transaction
    public List<Transactionn> getTransactionnsByDefault() {
        mTransactionns = DataSupport.findAll(Transactionn.class);
        Log.e("123123123123", mTransactionns.size() + "");
        return mTransactionns;
    }

    //得到时间排序好的transaction
    public List<Transactionn> getTransactionnsByTime() {
        mTransactionns = DataSupport.findAll(Transactionn.class);
        Collections.sort(mTransactionns);
        return mTransactionns;
    }

    //删除所有的transaction
    public void deleteTransactions() {
        int a = DataSupport.deleteAll(Transactionn.class);
        Log.e("Tsdfsdf", a + "");
    }

    //删除指定的transaction
    public int deleteTransaction(String uuid) {
        return DataSupport.deleteAll(Transactionn.class, "mUUID = ?", uuid);
    }

    //设置并得到刚拍摄照片的路径
    public File getPhotoFile(Transactionn transactionn, Context context) {
        File externalFileDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFileDir == null) {
            return null;
        }
        return new File(externalFileDir, transactionn.getPhotoName());
    }
}
