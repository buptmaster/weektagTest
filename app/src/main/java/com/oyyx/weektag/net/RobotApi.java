package com.oyyx.weektag.net;

import com.oyyx.weektag.dateBase.ChatRobot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 123 on 2017/9/15.
 */

public interface RobotApi {
    @GET("api")
    Call<ChatRobot> getChatInfo(@Query("key") String key, @Query("info") String info);
}
