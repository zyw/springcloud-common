package cn.v5cn.spark245.example

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 *
 */
object WordCountScala {
  def main(args: Array[String]): Unit = {

    // 1. 第一步：创建SparkConf对象
    val conf: SparkConf = new SparkConf().setAppName("WordCountScala")

    // 2. 第二步：创建SparkContext对象，使用SparkContext来创建RDD
    val sc: SparkContext = new SparkContext(conf)

    // 3. 第三步：创建RDD，RDD中是一行一行的单词
    val lines: RDD[String] = sc.textFile(args(0))

    // 4. 第四步：切分压平，RDD中是一个一个的单词
    val words: RDD[String] = lines.flatMap(_.split("\\s+"))

    // 5. 单词计数，RDD中是单词和个数
    val wordsAndOne: RDD[(String, Int)] = words.map((_,1))

    // 6. 分组聚合，reduceByKey先局部聚合在全局聚合
    val reduced: RDD[(String, Int)] = wordsAndOne.reduceByKey(_+_)

    // 7. 排序，按照元组的第二个元素排序
    val sorted: RDD[(String, Int)] = reduced.sortBy(_._2,false)

    // 8. 将计算结果保存到HDFS
    sorted.saveAsTextFile(args(1))

    // 9. 释放资源
    sc.stop()
  }
}
