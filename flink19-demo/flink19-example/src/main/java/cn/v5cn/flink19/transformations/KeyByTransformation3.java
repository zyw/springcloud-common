package cn.v5cn.flink19.transformations;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * keyBy传入多值，类似sql的grup by后面跟多个字段，可以根据多个字段进行分组。
 * 例子是各个省内市的GDP，单位千亿
 * @author ZYW
 * @version 1.0
 * @date 2020-02-20 21:38
 */
public class KeyByTransformation3 {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //source
        DataStreamSource<String> lines = env.fromElements("陕西,西安,8","陕西,西安,7","河北,石家庄,4","河北,石家庄,3");

        //flatMap Transformation是输入一个参数，不产生参数
        SingleOutputStreamOperator<Tuple3<String,String,Integer>> result = lines.flatMap(new FlatMapFunction<String, Tuple3<String,String,Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple3<String,String,Integer>> out) throws Exception {
                String[] words = line.split(",");
                out.collect(Tuple3.of(words[0],words[1],Integer.parseInt(words[2])));
            }
        });

        //KeyBy可以根据多个参数进行分组
        KeyedStream<Tuple3<String, String, Integer>, Tuple> keyByed = result.keyBy(0, 1);
        //sum聚合方法。
        SingleOutputStreamOperator<Tuple3<String, String, Integer>> summd = keyByed.sum(2);

        //Sink
        summd.print();

        env.execute("KeyByTransformation3");

//        输出结果
//        3> (陕西,西安,8)
//        3> (陕西,西安,15)
//        4> (河北,石家庄,4)
//        4> (河北,石家庄,7)
    }
}
