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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 123 on 2017/8/22.
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
                Transactionn transactionn = mTransactionns.get(transactionHolder.getAdapterPosition());
                Intent intent = new Intent(parent.getContext(), DetailActivity.class);
                intent.putExtra("transaction",transactionn);
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

        public TextView title_tv, memo_tv, remaining_days_tv;
        public FloatingActionButton color_fab;

        public FrameLayout title_dash_line;
        public CardView item_layout;

        public TransactionHolder(View view) {
            super(view);
            title_tv = (TextView) view.findViewById(R.id.history_title);
            memo_tv = (TextView) view.findViewById(R.id.history_memo);
            remaining_days_tv = (TextView) view.findViewById(R.id.tv_remaining_days);
            color_fab = (FloatingActionButton) view.findViewById(R.id.fab_color_use);

            title_dash_line = (FrameLayout) view.findViewById(R.id.dash_line_title);
            item_layout = (CardView) view.findViewById(R.id.item_layout);

            //加粗字体
            TextPaint paint = remaining_days_tv.getPaint();
            paint.setFakeBoldText(true);
            TextPaint paint1 = title_tv.getPaint();
            paint1.setFakeBoldText(true);


        }

        public void bindTransaction(Transactionn transactionn) {

            mTransactionn = transactionn;

            long[] longs = CalendarUtils.getTime(transactionn.getTime());

            if(longs[0]<=0){
                remaining_days_tv.setText(0+"");
                item_layout.setCardBackgroundColor(item_layout.getResources().getColor(R.color.cardview_dark_background));
            }else {
                remaining_days_tv.setText(longs[0] + "");
            }
            title_tv.setText(transactionn.getTitle());
            color_fab.setBackgroundTintList(ColorStateList.valueOf(transactionn.getColour()));
            if (transactionn.getMemo() != null) {
                memo_tv.setVisibility(View.VISIBLE);
                title_dash_line.setVisibility(View.VISIBLE);
                memo_tv.setText(transactionn.getMemo());
            }
        }
    }
}
