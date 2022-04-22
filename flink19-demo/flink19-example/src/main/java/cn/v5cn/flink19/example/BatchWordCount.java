package cn.v5cn.flink19.example;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.tuple.Tuple2;

import java.nio.charset.StandardCharsets;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-19 20:48
 */
public class BatchWordCount {
    public static void main(String[] args) throws Exception {
        //1. 创建上下文环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //2. 创建DataSet，DataSource继承自DataSet
        DataSource<String> dataSource = env.readTextFile(args[0], StandardCharsets.UTF_8.name());
        //3. 使用Transformation(s)完成计算
        FlatMapOperator<String, Tuple2<String, Integer>> wordAndOne = dataSource.name("BatchWordCount-tf")
                .<Tuple2<String, Integer>>flatMap((line, out) -> {
                    String[] words = line.split(" ");
                    for (String word : words) {
                        out.collect(Tuple2.of(word, 1));
                    }
                }).returns(Types.TUPLE(Types.STRING, Types.INT));
        //  和流式计算的区别是这里使用的是groupBy，而不是keyBy
        AggregateOperator<Tuple2<String, Integer>> summed = wordAndOne.name("BatchWordCount-group-sum").groupBy(0).sum(1);
        //4. sink 如果使用summed.print();不需要调用env.execute()方法，因为print方法内部会调用execute方法
        summed.print();
        //summed.writeAsText(args[1]);
        //5. 运行
        //env.execute("BatchWordCount");
    }
}
