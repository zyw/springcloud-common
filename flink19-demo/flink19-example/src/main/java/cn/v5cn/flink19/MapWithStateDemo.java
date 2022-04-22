package cn.v5cn.flink19;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 实现一个类似聚合函数的功能，并使用ValueState保存历史数据，并在SubTask回复时，回复以前保存的数据。
 * @author ZYW
 * @version 1.0
 * @date 2020-03-12 17:00
 */
public class MapWithStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //开启Checkpointing，5秒发送一次保存State命令
        env.enableCheckpointing(5000);

        //更改重启策略，设置对多重启3次，每次延迟2秒。
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3,2000));

        DataStreamSource<String> socketStream = env.socketTextStream("localhost", 9999);

        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOne = socketStream.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String word) throws Exception {
                if(word.startsWith("zhangsan")) {
                    throw new RuntimeException("抛异常了。");
                }
                return Tuple2.of(word,1);
            }
        });
        KeyedStream<Tuple2<String, Integer>, Tuple> keyed = wordAndOne.keyBy(0);

        SingleOutputStreamOperator<Tuple2<String, Integer>> summed = keyed.map(new RichMapFunction<Tuple2<String, Integer>, Tuple2<String, Integer>>() {

            private transient ValueState<Tuple2<String, Integer>> sumState;

            @Override
            public void open(Configuration parameters) throws Exception {

                ValueStateDescriptor<Tuple2<String, Integer>> state = new ValueStateDescriptor<Tuple2<String, Integer>>("custom"
                        , Types.TUPLE(Types.STRING, Types.INT));

                sumState = getRuntimeContext().getState(state);
            }

            @Override
            public Tuple2<String, Integer> map(Tuple2<String, Integer> value) throws Exception {
                Tuple2<String, Integer> hv = sumState.value();
                if(hv != null) {
                    hv.f1 += value.f1;
                    sumState.update(hv);
                    return hv;
                }
                sumState.update(value);
                return value;
            }
        });

        summed.print();

        env.execute("MapWithStateDemo");
    }
}
