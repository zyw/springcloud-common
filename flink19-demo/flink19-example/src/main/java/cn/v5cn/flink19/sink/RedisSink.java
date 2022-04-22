package cn.v5cn.flink19.sink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * 使用官方的RedisSink
 * <dependency>
 * 	<groupId>org.apache.bahir</groupId>
 * 	<artifactId>flink-connector-redis_2.11</artifactId>
 * 	<version>1.1-SNAPSHOT</version>
 * </dependency>
 * @author ZYW
 * @version 1.0
 * @date 2020-03-12 13:13
 */
public class RedisSink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> source = env.fromElements("hello you", "hello me", "hello world","hbase hadoop","zookeeper kafka");

        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOne = source.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String lines, Collector<Tuple2<String, Integer>> out) throws Exception {
                Arrays.stream(lines.split(" ")).forEach(word -> {
                    out.collect(Tuple2.of(word, 1));
                });
            }
        });

        SingleOutputStreamOperator<Tuple2<String, Integer>> summed = wordAndOne.keyBy(0).sum(1);


        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("127.0.0.1").setPort(6379).build();

        summed.addSink(new org.apache.flink.streaming.connectors.redis.RedisSink<Tuple2<String, Integer>>(conf,new RedisWordCountMapper()));


        env.execute("RedisSink");

    }

    public static class RedisWordCountMapper implements RedisMapper<Tuple2<String, Integer>> {

        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.HSET, "WORD_COUNT");
        }

        @Override
        public String getKeyFromData(Tuple2<String, Integer> data) {
            return data.f0;
        }

        @Override
        public String getValueFromData(Tuple2<String, Integer> data) {
            return String.valueOf(data.f1);
        }
    }
}
