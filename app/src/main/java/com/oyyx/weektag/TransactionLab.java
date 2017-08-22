package com.oyyx.weektag;


import android.util.Log;

import org.litepal.crud.DataSupport;

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
        mTransactionns = DataSupport.findAll(Transactionn.class);
        Log.e("----------->",""+mTransactionns.size());
    }

    public List<Transactionn> getTransactionns(){

        return mTransactionns;
    }

    public void deleteTransactions(){
        DataSupport.deleteAll(Transactionn.class, "title = ?");
    }

    public int deleteTransaction(String uuid){
       return DataSupport.deleteAll(Transactionn.class,"mUUID = ?",uuid);
    }



}
