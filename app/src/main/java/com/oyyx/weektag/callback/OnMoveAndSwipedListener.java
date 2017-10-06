package com.oyyx.weektag.callback;

/**
 * Created by 123 on 2017/10/5.
 */

public interface OnMoveAndSwipedListener {

    //移动item位置
    boolean onItemMove(int fromPosition, int toPosition);

    //滑动删除item
    void onItemDismiss(int position);
}
