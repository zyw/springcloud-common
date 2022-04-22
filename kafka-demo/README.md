# kafka2.2.0
## 单机安装
单机安装的kafka比较简单，解压后只需要2部就能启动好。因为kafka是zookeeper强依赖的所有需要先启动zookeeper。kafka有自带的zookeeper不需要单独下载。
1. 启动zookeeper，kafka解压目录。
   ```shell script
    ./bin/zookeeper-server-start.sh ./config/zookeeper.properties
   ```
2. 启动kafka。
   ```shell script
    ./bin/kafka-server-start.sh ./config/server.properties
   ```
> 因为都是本机启动所有不需要修改任何配置。

## 命令行操作
### 创建Topic
```shell script
./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic 主题名称(Topic)
```
### 生产者
```shell script
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic 主题名称(Topic)
```
### 消费者
```shell script
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic 主题名称(Topic) --from-beginning
```

## 集群配置

| 节点名称 | ip  |
| ---- | ---- |
| kafka1 | 192.168.33.101|
| kafka2 | 192.168.33.102 |
| kafka3 | 192.168.33.103 |

* 如上表所示，集群一共有三个节点，每个节点的配置都完全相同。
* 进入`kafka1`节点`kafka`的`config`目录，编辑`server.properties`文件的下面几个选项。
  ```properties
  broker.id=1  # kafka1，kafka2，kafka3 节点，依次设置为1，2，3
  zookeeper.connect=192.168.33.101:2181,192.168.33.102:2181,192.168.33.103:2181
  listeners = PLAINTEXT://192.168.33.101:9092     #本机的ip地址加9092端口
  ```
* 一定要设置`listeners`不然会报找不到leader的错误。
  ```text
  WARN [Producer clientId=console-producer] Error while fetching metadata with correlation id 3 : {test=LEADER_NOT_AVAILABLE} (org.apache.kafka.clients.NetworkClient)
  ```
* 三个节点均配置成功后，重启 kafka 和 zookeeper 集群即可正常运行

[原文地址](https://blog.csdn.net/qq_36431213/article/details/99363190)
