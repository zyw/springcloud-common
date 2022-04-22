package cn.v5cn.netty.im.common.rest;

import cn.v5cn.netty.im.common.domain.ResultWrapper;
import cn.v5cn.netty.im.common.exception.ImException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

/**
 *
 * @param <R>
 * @author yrw
 */
public abstract class AbstractRestService<R> {

    protected R restClient;

    public AbstractRestService(Class<R> clazz, String url) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        this.restClient = retrofit.create(clazz);
    }

    protected <T> T doRequest(RestFunction<T> function) {
        try {
            final Response<ResultWrapper<T>> response = function.doRequest();
            if(!response.isSuccessful()) {
                throw new ImException("[rest service] status is not 200, response body: " + response.toString());
            }
            if(response.body() == null) {
                throw new ImException("[rest service] response body is null");
            }
            if(response.body().getStatus() != HttpResponseStatus.OK.code()) {
                throw new ImException("[rest service] status is not 200, response body: " + new ObjectMapper().writeValueAsString(response.body()));
            }
            return response.body().getData();
        } catch (IOException e) {
            throw new ImException("[rest service] has error", e);
        }
    }

    @FunctionalInterface
    protected interface RestFunction<T> {
        /**
         * 执行一个http请求
         *
         * @return
         * @throws IOException
         */
        Response<ResultWrapper<T>> doRequest() throws IOException;
    }
}
