package cn.v5cn.mapreduce.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 每一行根据空格截取单词并返回。
 * map阶段输入的k1,v1 分别是k1是行号，v1是整行文本
 * map阶段返回的k2,v2 分别是k2是单词，v2是单词的数量
 *
 * 读取hdfs中的hello.txt文件，内容是：
 * hello you
 * hello me
 * hello world
 *
 * 最终的结果形式如下：
 * hello 3
 * me    1
 * you   1
 * world 1
 *
 * @author ZYW
 * @version 1.0
 * @date 2020-02-13 22:30
 */
public class WordCountMap extends Mapper<LongWritable,Text,Text,LongWritable> {

    /**
     * 需要实现map方法
     * 输入k1，v1 输出k2,v2
     * @param k1 是每一行的行首偏移量
     * @param v1 每一行的内容
     * @param context 框架上下文
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable k1, Text v1, Context context) throws IOException, InterruptedException {
        String lineText = v1.toString();
        //按照空格分隔行
        String[] lineArray = lineText.split(" ");
        for(String value : lineArray) {
            //写数据到reduce阶段
            context.write(new Text(value.trim()),new LongWritable(1));
        }
    }
}
