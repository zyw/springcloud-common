package cn.v5cn.flink19.sql;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;

import java.util.ArrayList;

/**
 * 使用Flink Table SQL编写WordCount
 * @author ZYW
 * @version 1.0
 * @date 2020-04-16 21:42
 */
public class WordCountSql {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //创建Table SQL环境
        BatchTableEnvironment fbTableEnv = BatchTableEnvironment.create(env);

        //读取一行模拟数据作为输入
        String words = "hello flink hello spark hello world";
        String[] split = words.split("\\W+");
        ArrayList<WC> list = new ArrayList<>();

        for (String word : split) {
            WC wc = new WC(word, 1);
            list.add(wc);
        }
        DataSet<WC> input = env.fromCollection(list);

        //注册成Table，执行SQL，然后输出
        //DataSet转Sql,指定字段名
        Table table = fbTableEnv.fromDataSet(input, "word,frequency");
        table.printSchema();

        //注册为一个表
        fbTableEnv.registerTable("WordCount",table);

        Table query = fbTableEnv.sqlQuery("SELECT word,sum(frequency) as frequency FROM WordCount GROUP BY word");

        //将表转换DataSet
        DataSet<WC> wcDataSet = fbTableEnv.toDataSet(query, WC.class);
        wcDataSet.printToErr();
    }

    public static class WC {
        public String word;
        public long frequency;

        public WC() {}

        public WC(String word, long frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        @Override
        public String toString() {
            return word + ", " + frequency;
        }
    }
}
