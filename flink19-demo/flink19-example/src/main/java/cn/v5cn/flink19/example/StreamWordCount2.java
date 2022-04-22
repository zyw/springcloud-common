package cn.v5cn.flink19.example;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.stream.Stream;

/**
 * <p>
 *     使用流式API编写，fluent api
 * </p>
 * @author ZYW
 * @version 1.0
 * @date 2020-02-18 21:10
 */
public class StreamWordCount2 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //1. source
        DataStream<String> source = env.socketTextStream(args[0], Integer.parseInt(args[1]));

        //2. Transformation(s)
//        DataStream<Tuple2<String, Integer>> summed = source.<String>flatMap((lines, out) -> Stream.of(lines.split(" ")).forEach(out::collect))
//                //指定返回类型
//                .returns(Types.STRING)
//                .map(word -> Tuple2.<String,Integer>of(word, 1))
//                //指定返回类型
//                .returns(Types.TUPLE(Types.STRING,Types.INT))
//                .keyBy(0)
//                .sum(1);
        //2. Transformation(s) 比上面的减少一个步骤,flatMap直接返回Tuple2类型
        DataStream<Tuple2<String, Integer>> summed = source.<Tuple2<String, Integer>>flatMap((lines, out) -> {
            Stream.of(lines.split(" ")).forEach(word -> {
                out.collect(Tuple2.of(word,1));
            });
        })
                //指定返回类型
                .returns(Types.TUPLE(Types.STRING,Types.INT))
                .keyBy(0)
                .sum(1);
        //3. sink
        summed.print();

        //4. 执行
        env.execute("StreamWordCount2");
    }
}
