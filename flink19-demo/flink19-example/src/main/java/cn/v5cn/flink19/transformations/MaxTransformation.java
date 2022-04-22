package cn.v5cn.flink19.transformations;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * 使用Max对内容进行聚合
 * @author ZYW
 * @version 1.0
 * @date 2020-02-20 21:38
 */
public class MaxTransformation {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //source
        DataStreamSource<String> lines = env.fromElements("hello 1","hello 5","hello 2","world 3","world 1");

        //Map Transformation是输入一个参数，经过map方法在产生一个参数。
        SingleOutputStreamOperator<Tuple2<String,Integer>> result = lines.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String,Integer>> out) throws Exception {
                String[] words = line.split(" ");
                out.collect(Tuple2.of(words[0],Integer.parseInt(words[1])));
            }
        });

        //按照零号元素进行分组，在这里0号元素就是指元祖中的第一个元素，Flink把元祖也当成一种数组，下标从零开始。
        KeyedStream<Tuple2<String, Integer>, Tuple> keyByed = result.keyBy(0);

        SingleOutputStreamOperator<Tuple2<String, Integer>> maxed = keyByed.max(1);

        //Sink
        maxed.print();

        env.execute("MaxTransformation");

//        输出结果，因为hello 5最大所有输出的是5，world也一样
//        2> (hello,5)
//        2> (hello,5)
//        2> (hello,5)
//        3> (world,3)
//        3> (world,3)
    }
}
