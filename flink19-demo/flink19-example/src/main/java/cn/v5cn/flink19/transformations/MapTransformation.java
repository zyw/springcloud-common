package cn.v5cn.flink19.transformations;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-20 20:32
 */
public class MapTransformation {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //source
        DataStreamSource<Integer> numsSource = env.fromElements(1,2,3,4,5);

        //Map Transformation是输入一个参数，经过map方法在产生一个参数。
        SingleOutputStreamOperator<Integer> result = numsSource.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer value) throws Exception {
                return value * 2;
            }
        });

        //Flink还提供了RichMapFunction，map方法也可以接受RichMapFunction这个功能更强大的接口。

        //Sink
        result.print();

        env.execute("MapTransformation");
    }
}
