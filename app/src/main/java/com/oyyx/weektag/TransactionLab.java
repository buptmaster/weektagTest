package com.oyyx.weektag;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by 123 on 2017/8/22.
 */

public class TransactionLab {

    private static TransactionLab sTransaction;

    private List<Transactionn> mTransactionns;

    public static TransactionLab get() {
        if (sTransaction == null) {
            sTransaction = new TransactionLab();
        }
        return sTransaction;
    }

    private TransactionLab(){
        
    }

    public List<Transactionn> getTransactionns(){
        mTransactionns = DataSupport.findAll(Transactionn.class);
        return mTransactionns;
    }

    public List<Transactionn> getTransactionnsByTime(){
        //mTransactionns = DataSupport.where("time > ?","0").order("colour").find(Transactionn.class);
        mTransactionns = DataSupport.findAll(Transactionn.class);
        Collections.sort(mTransactionns);
        return mTransactionns;
    }

    public void deleteTransactions(){
        DataSupport.deleteAll(Transactionn.class);
    }

    public int deleteTransaction(String uuid){
       return DataSupport.deleteAll(Transactionn.class,"mUUID = ?",uuid);
    }

    public File getPhotoFile(Transactionn transactionn, Context context) {
        File externalFileDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFileDir == null) {
            return null;
        }
        return new File(externalFileDir, transactionn.getPhotoName());
    }


}
