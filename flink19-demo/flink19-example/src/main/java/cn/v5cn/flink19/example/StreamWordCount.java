package cn.v5cn.flink19.example;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * <p>
 *     从Socket端口获得单词并统计个数。使用nc -lk 8888发送Socket数据
 * </p>
 * @author ZYW
 * @version 1.0
 * @date 2020-02-18 19:53
 */
public class StreamWordCount {

    //分步骤写法
    public static void main(String[] args) throws Exception {


        //1. 创建Flink执行上下文环境
        //StreamExecutionEnvironment是Data Stream的上下文环境。getExecutionEnvironment方法可以判断
        // 当前是集群模式还是本地模式，集群模式就会创建RemoteStreamEnvironment，本地模式创建LocalStreamEnvironment，本地模式的平行度是CPU合数，下面代码获得。
        // private static int defaultLocalParallelism = Runtime.getRuntime().availableProcessors();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();



        //2. 创建Source，Source可以有多个，但至少一个，这个是并行度为1的source
        //   使用StreamExecutionEnvironment的socketTextStream获得Source获得的数据是一行一行的。
        DataStream<String> source = env.socketTextStream("localhost", 8888);



        //3. 使用Transformation(s)处理数据。调用DataStream上的Transformation方法。 一个程序可以没有Transformation
        //   1). 处理一行一行的数据。得到一个trans
        DataStream<String> words = source.flatMap(new FlatMapFunction<String, String>() {
            /**
             * <p>
             *     处理一行数据
             * </p>
             * @param lines 一行文本
             * @param out 把处理的内容写出对象
             * @throws Exception 抛出异常
             */
            @Override
            public void flatMap(String lines, Collector<String> out) throws Exception {
                //按照空格切分
                String[] words = lines.split(" ");
                for (String word : words) {
                    //输出
                    out.collect(word);
                }
            }
        });
        //    2). 每个word标记为1，也是类型转换需要使用Flink内的元祖，第一个反向是word，第二个是word的数量。
        DataStream<Tuple2<String, Integer>> wordAndOne = words.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String word) throws Exception {
                return Tuple2.of(word, 1);
            }
        });
        //    3). 合并计算每个单词的数量,keyBy方法是按照Tuple2中的那个字段分组，跟数组相同是从0开始的
        DataStream<Tuple2<String, Integer>> summed = wordAndOne.keyBy(0)
                //按照那个字段计算
                .sum(1);



        //4. 使用Sink存储保存数据，必须有，在这只做输出。
        summed.print();

        //5. 启动程序，异常不要捕获，直接抛出
        env.execute("StreamWordCount");
    }
}
