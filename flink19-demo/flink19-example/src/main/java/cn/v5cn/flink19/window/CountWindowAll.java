package cn.v5cn.flink19.window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

/**
 * WindowAll是在不分组的情况下，对窗口条数计数
 * @author ZYW
 * @version 1.0
 * @date 2020-02-22 15:16
 */
public class CountWindowAll {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> socketTextStream = env.socketTextStream("localhost", 8888);

        SingleOutputStreamOperator<Integer> nums = socketTextStream.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String value) throws Exception {
                return Integer.parseInt(value);
            }
        });

        //在不分组的情况下，5条窗口计数。
        AllWindowedStream<Integer, GlobalWindow> windowAll = nums.countWindowAll(5);

        //在窗口中聚合
        SingleOutputStreamOperator<Integer> summed = windowAll.sum(0);

        summed.print();

        env.execute("CountWindowAll");
    }
}
