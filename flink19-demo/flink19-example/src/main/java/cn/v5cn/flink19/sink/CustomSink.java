package cn.v5cn.flink19.sink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * 自定义Sink，打印数据
 * @author ZYW
 * @version 1.0
 * @date 2020-02-21 16:21
 */
public class CustomSink {
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

        //自定义Sink
        summed.addSink(new RichSinkFunction<Tuple2<String, Integer>>() {
            @Override
            public void invoke(Tuple2<String, Integer> value, Context context) throws Exception {
                int subtask = getRuntimeContext().getIndexOfThisSubtask();
                System.out.println(subtask + " > " + value);
            }
        });

        env.execute("CustomSink");
    }
}
