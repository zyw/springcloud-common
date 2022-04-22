package cn.v5cn.flink19;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Flink Checkpointing和重启策略，并设置StateBackend
 * @author ZYW
 * @version 1.0
 * @date 2020-03-12 17:00
 */
public class StateBackendDemo {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //开启Checkpointing，5秒发送一次保存State命令
        env.enableCheckpointing(5000);

        //更改重启策略，设置对多重启3次，每次延迟2秒。
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3,2000));

        //设置StateBackend，FsStateBackend是使用文件系统保存State，可以是本地文件或者是hdfs。
        env.setStateBackend((StateBackend)new FsStateBackend("file:///Users/zhuyanwei/workspace/springcloud-common/flink19-demo/ck"));

        //程序异常退出或者认为cancel掉，Checkpoint的State不删除，默认是删除CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

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

        SingleOutputStreamOperator<Tuple2<String, Integer>> summed = wordAndOne.keyBy(0).sum(1);

        summed.print();

        env.execute("RestartStartDemo");
    }
}
