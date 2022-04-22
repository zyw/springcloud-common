package cn.v5cn.flink19.window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * 聚合后的TumblingWindow，聚合收每一个聚合结果达到指定时间。
 * @author ZYW
 * @version 1.0
 * @date 2020-02-22 15:16
 */
public class TumblingWindow {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //输入内容为：
        // hello 2
        // world 3
        DataStreamSource<String> socketTextStream = env.socketTextStream("localhost", 8888);

        SingleOutputStreamOperator<Tuple2<String,Integer>> words = socketTextStream.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String,Integer> map(String value) throws Exception {
                String[] words = value.split(" ");
                return Tuple2.of(words[0],Integer.parseInt(words[1]));
            }
        });

        KeyedStream<Tuple2<String, Integer>, Tuple> keyed = words.keyBy(0);
        //分组后进行TumblingWindow
        // WindowedStream<Tuple2<String, Integer>, Tuple, TimeWindow> window = keyed.timeWindow(Time.seconds(5));
        // 与上面方法等价
        WindowedStream<Tuple2<String, Integer>, Tuple, TimeWindow> window = keyed.window(TumblingProcessingTimeWindows.of(Time.seconds(5)));
        //进行窗口汇总
        SingleOutputStreamOperator<Tuple2<String, Integer>> summed = window.sum(1);
        //打印计数
        summed.print();

        env.execute("TumblingWindow");
    }
}
