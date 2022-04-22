package cn.v5cn.netty.im.connector.service.rest;

import cn.v5cn.netty.im.common.domain.ResultWrapper;
import cn.v5cn.netty.im.common.domain.po.Offline;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface ConnectorRestClient {

    @GET("/offline/poll/{id}")
    Call<ResultWrapper<List<Offline>>> pollOfflineMsg(@Path("id") String userId);
}
