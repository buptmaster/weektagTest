package com.oyyx.weektag.net;

import com.oyyx.weektag.adapter.HistoryAdapter;
import com.oyyx.weektag.dateBase.HistoryToday;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 123 on 2017/9/15.
 */

public interface HistoryApi {
    @GET("119-42")
    Call<HistoryToday> getHistoryToday(@Query("showapi_appid") String appid, @Query("showapi_sign") String code);
}
