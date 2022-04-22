package cn.v5cn.flink19.transformations;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-20 20:40
 */
public class FlatMapTransformation {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //source
        DataStreamSource<String> lines = env.fromElements("hello you","hello me","hello world");

        //Map Transformation是输入一个参数，经过map方法在产生一个参数。
        SingleOutputStreamOperator<String> result = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String line, Collector<String> out) throws Exception {
                Arrays.stream(line.split(" ")).forEach(out::collect);
            }
        });

        //Sink
        result.print();

        env.execute("FlatMapTransformation");
    }
}
