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
 * 使用Reduce对内容进行聚合
 * @author ZYW
 * @version 1.0
 * @date 2020-02-20 21:38
 */
public class ReduceTransformation {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //source
        DataStreamSource<String> lines = env.fromElements("hello you","hello me","hello world");

        //Map Transformation是输入一个参数，经过map方法在产生一个参数。
        SingleOutputStreamOperator<Tuple2<String,Integer>> result = lines.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String,Integer>> out) throws Exception {
                Arrays.stream(line.split(" ")).forEach(word -> {
                        out.collect(Tuple2.of(word,1));
                });
            }
        });

        //按照零号元素进行分组，在这里0号元素就是指元祖中的第一个元素，Flink把元祖也当成一种数组，下标从零开始。
        KeyedStream<Tuple2<String, Integer>, Tuple> keyByed = result.keyBy(0);

        //使用Reduce对单词数累加，类似与sum功能，但是Reduce可以根据业务写不同的聚合方式，相对sum就只能做累加运算。
        SingleOutputStreamOperator<Tuple2<String, Integer>> reduceed = keyByed.reduce(new ReduceFunction<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                value1.f1 += value2.f1;
                return value1;
            }
        });

        //Sink
        reduceed.print();

        env.execute("ReduceTransformation");

//        输出结果，是单词和数量，因为没有聚合所有只是单独的罗列
//        3> (world,1)
//        3> (you,1)
//        2> (hello,1)
//        2> (me,1)
//        2> (hello,2)
//        2> (hello,3)
    }
}
