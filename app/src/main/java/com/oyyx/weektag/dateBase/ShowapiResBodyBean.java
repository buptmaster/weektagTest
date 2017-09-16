package com.oyyx.weektag.dateBase;

import java.util.List;

/**
 * Created by 123 on 2017/9/16.
 */

public class ShowapiResBodyBean {


    private int ret_code;
    private List<ListBean> list;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }
}