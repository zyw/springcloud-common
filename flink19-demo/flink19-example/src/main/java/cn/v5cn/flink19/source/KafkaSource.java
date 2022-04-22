package cn.v5cn.flink19.source;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * <p>
 *     FlinkKafkaConsumer是一个并发Source因为他实现了ParallelSourceFunction接口
 * </p>
 * @author ZYW
 * @version 1.0
 * @date 2020-02-20 12:54
 */
public class KafkaSource {
    public static void main(String[] args) throws Exception {
        //1.创建Stream Env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.创建FlinkKafkaConsumer也就是kafka的source
        Properties properties = new Properties();
        //服务地址
        properties.setProperty("bootstrap.servers", "localhost:9092");
        //消费组
        properties.setProperty("group.id", "flink-kafka");

        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>("flink-topic",new SimpleStringSchema(),properties);
        DataStreamSource<String> streamSource = env.addSource(kafkaSource,"kafka-source");

        //并发数是4
        System.out.println("------------->"+streamSource.getParallelism());

        //读取内容直接打印不做处理
        streamSource.print();

        env.execute("KafkaSource");
    }
}
