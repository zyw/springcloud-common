## Hadoop3.x安装
### 配置主机名
1. 修改`hostname`为`hadoop001`
2. 在hosts中添加`hadoop001`到IP地址的映射
### 配置免密登录
1.第一步产生公钥和私钥，一路回车就可以了
```shell script
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
```
执行上面后会产生`id_rsa`(私钥)和`id_rsa.pub`(公钥)
2.第二步创建`authorized_keys`
```shell script
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```
3.第三步
```shell script
chmod 0600 ~/.ssh/authorized_keys
```
#### 密码登录别的主机
执行如下：
```shell script
ssh-copy-id -i 主机名或者主机IP
```
也可以指定公钥
```shell script
ssh-copy-id -i ~/.ssh/id_rsa.pub username@ip
```
### 伪分布式安装
Hadoop的配置文件在解压文件的`etc/hadoop`目录下。
#### 配置`hadoop-env.sh`
```shell script
# JDK配置
export JAVA_HOME=/data/soft/jdk1.8
# 日志路径配置
export HADOOP_LOG_DIR=/data/hadoop_repo/logs/hadoop
```
#### 配置`core-site.xml`
```xml
<configuration>
    <!-- hdfs地址 hadoop100为主机名也可以配置成localhost -->
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://hadoop001:9000</value>
    </property>
    <!-- 
     hadoop 全部临时目录的基础目录，
     hdfs-site.xml中的
     dfs.datanode.data.dir和dfs.namenode.name.dir
     的路径配置中就依赖这个配置
     file://${hadoop.tmp.dir}/dfs/data
    -->
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/data/hadoop_repo</value>
    </property>
</configuration>
```
#### 配置`hdfs-site.xml`
```xml
<configuration>
    <!-- 数据存储的副本数 -->
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>
```
#### 配置`mapred-site.xml`
```xml
<configuration>
    <!-- mapreduce的计算框架配置，这里是yarn -->
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <!-- mapreduce的classpath -->
    <property>
        <name>mapreduce.application.classpath</name>
        <value>$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/*:$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/*</value>
    </property>
</configuration>
```
#### 配置`yarn-site.xml`
```xml
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
</configuration>
```
#### 格式化`namenode`
执行
```shell script
bin/hdfs namenode -format
```

#### 启动hadoop集群
```shell script
sbin/start-all.sh
```
##### 错误(如果报以下错误)
```text
ERROR: Attempting to operate on hdfs namenode as root
ERROR: but there is no HDFS_NAMENODE_USER defined. Aborting operation.
Starting datanodes
ERROR: Attempting to operate on hdfs datanode as root
ERROR: but there is no HDFS_DATANODE_USER defined. Aborting operation.
Starting secondary namenodes [hadoop100]
ERROR: Attempting to operate on hdfs secondarynamenode as root
ERROR: but there is no HDFS_SECONDARYNAMENODE_USER defined. Aborting operation.
2019-07-25 10:04:25,993 WARN util.NativeCodeLoader: Unable to load native-hadoop library for
your platform... using builtin-java classes where applicable
Starting resourcemanager
ERROR: Attempting to operate on yarn resourcemanager as root
ERROR: but there is no YARN_RESOURCEMANAGER_USER defined. Aborting operation.
Starting nodemanagers
ERROR: Attempting to operate on yarn nodemanager as root
ERROR: but there is no YARN_NODEMANAGER_USER defined. Aborting operation.
```
**发现在启动的时候报错，提示缺少HDFS 和YARN 的一些用户信息。**
###### 解决方案如下：
1. 修改`start-dfs.sh`，`stop-dfs.sh`这两个脚本文件，在文件前面增加如下内容
```shell script
[root@hadoop100 hadoop]# cd /data/soft/hadoop-3.2.0/sbin
[root@hadoop100 sbin]# vi start-dfs.sh
HDFS_DATANODE_USER=root
HDFS_DATANODE_SECURE_USER=hdfs
HDFS_NAMENODE_USER=root
HDFS_SECONDARYNAMENODE_USER=root
[root@hadoop100 sbin]# vi stop-dfs.sh
HDFS_DATANODE_USER=root
HDFS_DATANODE_SECURE_USER=hdfs
HDFS_NAMENODE_USER=root
HDFS_SECONDARYNAMENODE_USER=root
```

2. 修改`start-yarn.sh`，`stop-yarn.sh`这两个脚本文件，在文件前面增加如下内容
```shell script
[root@hadoop100 sbin]# vi start-yarn.sh
YARN_RESOURCEMANAGER_USER=root
HADOOP_SECURE_DN_USER=yarn
YARN_NODEMANAGER_USER=root
[root@hadoop100 sbin]# vi stop-yarn.sh
YARN_RESOURCEMANAGER_USER=root
HADOOP_SECURE_DN_USER=yarn
YARN_NODEMANAGER_USER=root
```
##### 启动完成后执行`jps`命令
看到下面进程就表示启动完成了。总共5个服务
```shell script
DataNode
NameNode
ResourceManager
NodeManager
SecondaryNameNode
```
同时也可以访问页面来查看服务。
* hdfs web 界面：http://192.168.33.17:9870
* yarn web 界面：http://192.168.33.17:8088
### 分布式安装
#### 准备三台机器
ip 为：
* 192.168.33.100
* 192.168.33.101
* 192.168.33.102
对应的hostname 为：
* hadoop100
* hadoop101
* hadoop102
#### 配置免密登录和安装JDK
参见其他文档
#### core-site.xml配置
```xml
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://hadoop100:9000</value>
    </property>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/data/hadoop_repo</value>
    </property>
</configuration>
```
#### hdfs-site.xml配置
```xml
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>2</value>
    </property>
    <property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>hadoop100:50090</value>
    </property>
</configuration>
```
#### mapred-site.xml配置
```xml
<configuration>
    <!-- mapreduce的计算框架配置，这里是yarn -->
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <!-- mapreduce的classpath -->
    <property>
        <name>mapreduce.application.classpath</name>
        <value>$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/*:$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/*</value>
    </property>
</configuration>
```
#### yarn-site.xml配置
```xml
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>hadoop100</value>
    </property>
</configuration>
```
#### 配置workers文件，这个里面配置的是数据节点地址
```text
hadoop101
hadoop102
```
配置完成，可以启动集群了。

## 以WordCount为例，MapReduce的过程
![WordCount](./image/1.png)

## 包MapReduce任务

```shell script
hadoop jar hadoop3-demo-mapreduce-1.0.0.jar \ 需要执行的MapReduce jar
 cn.v5cn.mapreduce.wordcount.WordCountJob \ Job的全路径类名
 hdfs://hadoop2:9000/hello.txt \ 输入路径
 hdfs://hadoop2:9000/out  输出路径
```

## 跑MapReduce时报的异常
```text
2020-02-14 06:19:50,337 INFO mapreduce.Job: Job job_1581660411968_0004 running in uber mode : false
2020-02-14 06:19:50,338 INFO mapreduce.Job:  map 0% reduce 0%
2020-02-14 06:19:50,364 INFO mapreduce.Job: Job job_1581660411968_0004 failed with state FAILED due to: Application application_1581660411968_0004 failed 2 times due to AM Container for appattempt_1581660411968_0004_000002 exited with  exitCode: 127
Failing this attempt.Diagnostics: [2020-02-14 06:19:49.685]Exception from container-launch.
Container id: container_1581660411968_0004_02_000001
Exit code: 127

[2020-02-14 06:19:49.689]Container exited with a non-zero exit code 127. Error file: prelaunch.err.
Last 4096 bytes of prelaunch.err :
Last 4096 bytes of stderr :
/bin/bash: /bin/java: No such file or directory


[2020-02-14 06:19:49.689]Container exited with a non-zero exit code 127. Error file: prelaunch.err.
Last 4096 bytes of prelaunch.err :
Last 4096 bytes of stderr :
/bin/bash: /bin/java: No such file or directory
```
### 解决方案
在系统的`bin`目录下创建一个`java`的软连接
```shell script
cd /bin
# /usr/local/jdk8/bin/java java的安装路径
sudo ln -s /usr/local/jdk8/bin/java java
```