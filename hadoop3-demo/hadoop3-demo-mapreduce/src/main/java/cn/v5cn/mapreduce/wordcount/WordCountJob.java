package cn.v5cn.mapreduce.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 组装Job=map+reduce
 * @author ZYW
 * @version 1.0
 * @date 2020-02-13 22:46
 */
public class WordCountJob {
    public static void main(String[] args) {
        try {
            //args应该包含输入和输出路径
            if(args.length != 2) {
                System.exit(100);
            }
            //job配置类
            Configuration conf = new Configuration();

            //创建job
            Job job = Job.getInstance(conf);

            //注意：必须设置这个属性，不设置会在启动时报找不到该类
            job.setJarByClass(WordCountJob.class);

            //设置输入路径（可以是文件，也可以是目录）
            FileInputFormat.setInputPaths(job,new Path(args[0]));

            //设置输出路径(只能指定一个不存在的目录)
            FileOutputFormat.setOutputPath(job,new Path(args[1]));

            //map设置
            job.setMapperClass(WordCountMap.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(LongWritable.class);

            //reduce设置
            job.setReducerClass(WordCountReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(LongWritable.class);

            //提交job
            job.waitForCompletion(true);

        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
