package cn.v5cn.flink19.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-03-26 15:54
 */
public class FlinkUtils {

    private static StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static StreamExecutionEnvironment getEnv() {
        return env;
    }

    public static <T> DataStreamSource<T> createKafkaConsumer(ParameterTool params, DeserializationSchema<T> schema) {
        long Interval = params.getLong("flink.checkpoint.interval", 5000);
        //开启Checkpointing，同时设置重启策略
        env.enableCheckpointing(Interval, CheckpointingMode.EXACTLY_ONCE);

        //生产环境全局配置文件指定(测试时使用)
        boolean stateBackendTest = params.getBoolean("flink.statebackend.test", false);
        if(stateBackendTest) {
            String path = params.getRequired("flink.statebackend.path");
            env.setStateBackend((StateBackend) new FsStateBackend(path));
        }

        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3,2000));

        //取消任务Checkpoint不删除
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        //2.创建FlinkKafkaConsumer也就是kafka的source
        Properties props = new Properties();
        //服务地址
        props.setProperty("bootstrap.servers", params.getRequired("kafka.bootstrap.servers"));
        //消费组
        props.setProperty("group.id", params.getRequired("kafka.group.id"));
        //如果没有记录偏移量，第一次从最开始消费
        props.setProperty("auto.offset.reset",params.get("kafka.auto.offset.reset","earliest"));
        //kafka的消费者不自动提交偏移量(false)
        props.setProperty("enable.auto.commit",params.get("kafka.enable.auto.commit","false"));

        String topics = params.getRequired("kafka.topics");

        if(StringUtils.isEmpty(topics)) {
            throw new RuntimeException("kafka.topics is empty!");
        }

        List<String> topicList = Arrays.asList(StringUtils.split(topics, ","));

        FlinkKafkaConsumer<T> kafkaSource = new FlinkKafkaConsumer<T>(topicList,schema,props);
        return env.addSource(kafkaSource,"kafka-consumer");
    }
}
