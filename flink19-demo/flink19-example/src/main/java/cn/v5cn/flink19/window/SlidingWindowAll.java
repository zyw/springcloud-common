package cn.v5cn.flink19.window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * 滑动窗口
 * @author ZYW
 * @version 1.0
 * @date 2020-02-22 16:16
 */
public class SlidingWindowAll {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> socketTextStream = env.socketTextStream("localhost", 8888);

        SingleOutputStreamOperator<Integer> nums = socketTextStream.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String value) throws Exception {
                return Integer.parseInt(value);
            }
        });

        //在不分组的情况下，5秒窗口滑动
        AllWindowedStream<Integer, TimeWindow> windowAll = nums.timeWindowAll(Time.seconds(10), Time.seconds(5));

        //在窗口中聚合
        SingleOutputStreamOperator<Integer> summed = windowAll.sum(0);

        summed.print();

        env.execute("SlidingWindowAll");
    }
}
