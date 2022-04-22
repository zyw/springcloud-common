package cn.v5cn.flink19.sink;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import redis.clients.jedis.Jedis;

/**
 * 自定义Redis Sink
 * @author ZYW
 * @version 1.0
 * @date 2020-03-26 21:04
 */
public class MyRedisSink extends RichSinkFunction<Tuple3<String, String, String>> {

    private Jedis jedis;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        //从全局配置中获取ParameterTool
        ParameterTool params = (ParameterTool)getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        String host = params.getRequired("redis.host");
        int port = params.getInt("redis.port", 6379);
        int database = params.getInt("redis.database", 0);
        String pwd = params.get("redis.password", "");
        jedis = new Jedis(host,port,5000);
        if(StringUtils.isNotEmpty(pwd)) {
            jedis.auth(pwd);
        }
        jedis.select(database);
    }

    @Override
    public void invoke(Tuple3<String, String, String> value, Context context) throws Exception {
        if(jedis.isConnected()) {
            jedis.connect();
        }
        jedis.hset(value.f0,value.f1,value.f2);
    }

    @Override
    public void close() throws Exception {
        super.close();
        jedis.close();
    }
}
