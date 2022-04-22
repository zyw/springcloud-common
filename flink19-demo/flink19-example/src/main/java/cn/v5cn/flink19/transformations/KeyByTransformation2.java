package cn.v5cn.flink19.transformations;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * 不使用Tuple2，自定义数据类型WordCount
 * @author ZYW
 * @version 1.0
 * @date 2020-02-20 21:38
 */
public class KeyByTransformation2 {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //source
        DataStreamSource<String> lines = env.fromElements("hello you","hello me","hello world");

        //Map Transformation是输入一个参数，经过map方法在产生一个参数。
        SingleOutputStreamOperator<WordCount> result = lines.flatMap(new FlatMapFunction<String, WordCount>() {
            @Override
            public void flatMap(String line, Collector<WordCount> out) throws Exception {
                Arrays.stream(line.split(" ")).forEach(word -> {
                        out.collect(new WordCount(word,1));
                });
            }
        });

        //需要使用keyBy的重载方法完成，通过属性名称完成，内部使用反射
        KeyedStream<WordCount, Tuple> keyed = result.keyBy("word");
        //sum聚合方法也有同样的重载方法。
        SingleOutputStreamOperator<WordCount> summd = keyed.sum("counts");

        //Sink
        summd.print();

        env.execute("KeyByTransformation2");

//        输出结果，是单词和数量
//        2> WordCount{word='hello', counts=1}
//        2> WordCount{word='me', counts=1}
//        3> WordCount{word='you', counts=1}
//        2> WordCount{word='hello', counts=2}
    }
}
