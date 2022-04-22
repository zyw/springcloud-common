package cn.v5cn.netty.im.client.service;

import cn.v5cn.netty.im.client.domain.UserReq;
import cn.v5cn.netty.im.common.domain.ResultWrapper;
import cn.v5cn.netty.im.common.domain.UserInfo;
import cn.v5cn.netty.im.common.domain.po.RelationDetail;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * @author yrw
 */
public interface ClientRestClient {

    @Headers("Content-Type: application/json")
    @POST("/user/login")
    Call<ResultWrapper<UserInfo>> login(@Body UserReq user);

    @POST("/user/logout")
    Call<ResultWrapper<Void>> logout(@Header("token") String token);

    @GET("/relation/{id}")
    Call<ResultWrapper<List<RelationDetail>>> friends(@Path("id") String userId, @Header("token")String token);

    @GET("/relation")
    Call<ResultWrapper<RelationDetail>> relation(
            @Query("userId")String userId, @Query("userId2")String userId2,
            @Header("token")String token
    );
}
