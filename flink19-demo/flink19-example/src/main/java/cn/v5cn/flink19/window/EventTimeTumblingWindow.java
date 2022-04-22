package cn.v5cn.flink19.window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * EventTimeTumblingWindow(滚动窗口)数据之间如果达到指定的时间间隔，划分为一个window
 * @author ZYW
 * @version 1.0
 * @date 2020-02-22 16:18
 */
public class EventTimeTumblingWindow {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //指定使用EventTime作为时间标准
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //输入内容为：
        //时间戳         单词   数量
        //1582372563000 world 3
        SingleOutputStreamOperator<String> socketTextStream = env.socketTextStream("localhost", 8888)
                //创建一个时间提取器，提取第一个时间字段，对每行的数据不产生影响 Time.seconds是延迟时间
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<String>(Time.seconds(0)) {
                    @Override
                    public long extractTimestamp(String line) {
                        return Long.parseLong(line.split(" ")[0]);
                    }
                });

        SingleOutputStreamOperator<Tuple2<String,Integer>> words = socketTextStream.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String,Integer> map(String value) throws Exception {
                String[] words = value.split(" ");
                return Tuple2.of(words[1],Integer.parseInt(words[2]));
            }
        });

        KeyedStream<Tuple2<String, Integer>, Tuple> keyed = words.keyBy(0);
        //分组后进行TumblingEventTimeWindows，滚动时间是5秒
        WindowedStream<Tuple2<String, Integer>, Tuple, TimeWindow> window = keyed.window(TumblingEventTimeWindows.of(Time.seconds(5)));
        //进行窗口计数
        SingleOutputStreamOperator<Tuple2<String, Integer>> summed = window.sum(1);
        //打印计数
        summed.print();

        env.execute("EventTimeTumblingWindow");
    }
}
