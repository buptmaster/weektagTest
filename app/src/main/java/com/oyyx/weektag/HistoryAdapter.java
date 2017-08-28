package com.oyyx.weektag;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

/**
 * Created by 123 on 2017/8/22.adapter
 * MainActivity的一个adapter
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.TransactionHolder> {

    private List<Transactionn> mTransactionns;

    public HistoryAdapter(List<Transactionn> mTransactionns){
        this.mTransactionns = mTransactionns;
    }


    @Override
    public TransactionHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        final TransactionHolder transactionHolder = new TransactionHolder(view);
        transactionHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Transactionn transactionn = mTransactionns.get(transactionHolder.getAdapterPosition());
                Intent intent = new Intent(parent.getContext(), DetailActivity.class);
                intent.putExtra("position",transactionHolder.getAdapterPosition());
                parent.getContext().startActivity(intent);
            }
        });
        return transactionHolder;
        }

    @Override
    public void onBindViewHolder(HistoryAdapter.TransactionHolder holder, int position) {
        Transactionn transactionn = mTransactionns.get(position);
        holder.bindTransaction(transactionn);
    }

    @Override
    public int getItemCount() {
        return mTransactionns.size();
    }

    public void setTransactionns(List<Transactionn> transactionns){
        mTransactionns = transactionns;
    }

    class TransactionHolder extends RecyclerView.ViewHolder {


        Transactionn mTransactionn;

        private TextView title_tv, memo_tv, remaining_days_tv,unit;
        private FloatingActionButton color_fab;

        private CardView item_layout;

        private ImageView date_bg;

        private View mView;



        public TransactionHolder(View view) {
            super(view);

            mView = view;
            title_tv = (TextView) view.findViewById(R.id.history_title);
            memo_tv = (TextView) view.findViewById(R.id.history_memo);
            remaining_days_tv = (TextView) view.findViewById(R.id.tv_remaining_days);
            unit = (TextView) view.findViewById(R.id.unit_transaction);

            color_fab = (FloatingActionButton) view.findViewById(R.id.fab_color_use);

            item_layout = (CardView) view.findViewById(R.id.item_layout);

            date_bg = (ImageView) view.findViewById(R.id.date_bg);


        }

        //绑定事件
        public void bindTransaction(Transactionn transactionn) {

            mTransactionn = transactionn;

            long[] longs = CalendarUtils.getTime(transactionn.getTime());

            if(longs[0]==0){
                if(longs[1]==0){
                    remaining_days_tv.setText("0");
                    unit.setText("小时");
                    Glide.with(mView).load(R.drawable.red_bg).into(date_bg);
                }else{
                    remaining_days_tv.setText(longs[1]+"");
                    unit.setText("小时");
                    Glide.with(mView).load(R.drawable.yellow_bg).into(date_bg);
                }
            }else {
                remaining_days_tv.setText(longs[0] + "");
                unit.setText("天");
                Glide.with(mView).load(R.drawable.date_bg).into(date_bg);
            }
            title_tv.setText(transactionn.getTitle());
            color_fab.setBackgroundTintList(ColorStateList.valueOf(transactionn.getColour()));
            if (!transactionn.getMemo().equals("")) {
                memo_tv.setText(transactionn.getMemo());
            }else {
                Random random = new Random();
                int[] strs = {R.string.good_thing, R.string.bad_thing};
                memo_tv.setText(strs[random.nextInt(1)]);
            }
        }
    }
}
