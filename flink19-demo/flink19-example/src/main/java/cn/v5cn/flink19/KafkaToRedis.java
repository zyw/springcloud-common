package cn.v5cn.flink19;

import cn.v5cn.flink19.sink.MyRedisSink;
import cn.v5cn.flink19.util.FlinkUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-03-26 16:38
 */
public class KafkaToRedis {

    public static void main(String[] args) throws Exception {
        ParameterTool params = ParameterTool.fromPropertiesFile(args[0]);

        //设置ParameterTool为全局配置
        FlinkUtils.getEnv().getConfig().setGlobalJobParameters(params);

        DataStreamSource<String> kafkaConsumer = FlinkUtils.createKafkaConsumer(params, new SimpleStringSchema());

        SingleOutputStreamOperator<Tuple2<String, Integer>> wordCount = kafkaConsumer.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String value) throws Exception {
                return Tuple2.of(value, 1);
            }
        });

        SingleOutputStreamOperator<Tuple2<String, Integer>> summed = wordCount.keyBy(0).sum(1);

        summed.map(new MapFunction<Tuple2<String, Integer>, Tuple3<String,String,String>>() {
            @Override
            public Tuple3<String, String, String> map(Tuple2<String, Integer> value) throws Exception {
                return Tuple3.of("word_count",value.f0,value.f1.toString());
            }
        })
                //添加自定义RedisSink
                .addSink(new MyRedisSink());

        FlinkUtils.getEnv().execute("KafkaToRedis");
    }
}
