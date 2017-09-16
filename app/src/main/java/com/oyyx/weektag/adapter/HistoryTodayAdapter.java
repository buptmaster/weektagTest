package com.oyyx.weektag.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oyyx.weektag.R;
import com.oyyx.weektag.dateBase.ListBean;

import java.util.List;

/**
 * Created by 123 on 2017/9/15.
 */

public class HistoryTodayAdapter extends RecyclerView.Adapter<HistoryTodayAdapter.HistoryViewHolder> {

    private Context mContext;
    private List<ListBean> mListBeen;


    public HistoryTodayAdapter(List<ListBean> listBean) {
        mListBeen = listBean;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_history_today, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        ListBean listBean = mListBeen.get(position);
        Log.e("itemmmmmmm", listBean.toString());
        holder.bindHistoryToday(listBean, mContext);
    }

    @Override
    public int getItemCount() {
        return mListBeen.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mTextViewTitle;
        private TextView mTextViewDate;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.item_history_image);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.item_history_text);
            mTextViewDate = (TextView) itemView.findViewById(R.id.item_history_text_date);

        }

        public void bindHistoryToday(ListBean listBean, Context context ) {
            if(listBean.getImg()!=null){
                mImageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(listBean.getImg()).into(mImageView);
            }else {
                mImageView.setVisibility(View.GONE);
            }
            if (listBean.getTitle()!=null) {
                mTextViewTitle.setText(listBean.getTitle());
                mTextViewDate.setText(listBean.getYear() + "年" + listBean.getMonth() + "月" + listBean.getDay() + "日");
            }
        }
    }
}
