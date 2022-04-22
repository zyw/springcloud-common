## spring-boot-starter-rocketmq使用
参考文章[https://weibo.com/ttarticle/p/show?id=2309404308844288593697](https://weibo.com/ttarticle/p/show?id=2309404308844288593697)

官方github地址[https://github.com/apache/rocketmq-spring](https://github.com/apache/rocketmq-spring)

### RocketMQ广播模式

对于RocketMQ同一个消费组下(`Consumer Group`)的消息监听在广播模式下会得到相同的消息。

使用注解的方式是：`@RocketMQMessageListener(topic = Constants.SPRING_TOPIC,consumerGroup = Constants.SPRING_TOPIC_CONSUMER,messageModel = MessageModel.BROADCASTING)`

即：`MessageModel.BROADCASTING`
## RocketMQ Docker搭建
1. 下载官方docker构建文件[https://github.com/apache/rocketmq-externals](https://github.com/apache/rocketmq-externals)
    官方文件中包含`rocketmq-docker`为我们所需要的Docker搭建项目。
    `rocketmq-console`为RocketMQ控制台项目也可以使用Docker构建
2. 我们使用`play-docker-compose.sh`部署，需要修改两个文件
   a) `4.3.0/docker-compose/rocketmq-namesrv/Dockerfile`
   
     修改前：
     ```shell
     FROM apache/rocketmq-base:4.3.0
     
     EXPOSE 9876
     
     RUN mv ${ROCKETMQ_HOME}/bin/runserver-customize.sh ${ROCKETMQ_HOME}/bin/runserver.sh \
      && chmod +x ${ROCKETMQ_HOME}/bin/runserver.sh \
      && chmod +x ${ROCKETMQ_HOME}/bin/mqnamesrv
     
     CMD cd ${ROCKETMQ_HOME}/bin \
      && export JAVA_OPT=" -Duser.home=/opt" \
      && sh mqnamesrv 
     ```
     修改后：
     ```shell
     FROM apache/rocketmq-base:4.3.0
     
     EXPOSE 9876
     
     RUN mv ${ROCKETMQ_HOME}/bin/runserver-customize.sh ${ROCKETMQ_HOME}/bin/runserver.sh \
      && chmod +x ${ROCKETMQ_HOME}/bin/runserver.sh \
      && chmod +x ${ROCKETMQ_HOME}/bin/mqnamesrv
     
     
     CMD cd ${ROCKETMQ_HOME}/bin \
      && export JAVA_OPT=" -Duser.home=/opt" \
      && sh mqnamesrv -n 154.8.143.230:9876  #宿主机的IP
     ```
   b) `4.3.0/docker-compose/rocketmq-broker/Dockerfile`
   
     修改前：
     ```shell
     FROM apache/rocketmq-base:4.3.0
     
     EXPOSE 10909 10911
     
     RUN mv ${ROCKETMQ_HOME}/bin/runbroker-customize.sh ${ROCKETMQ_HOME}/bin/runbroker.sh \
      && chmod +x ${ROCKETMQ_HOME}/bin/runbroker.sh \
      && chmod +x ${ROCKETMQ_HOME}/bin/mqbroker
     
     CMD cd ${ROCKETMQ_HOME}/bin \
      && export JAVA_OPT=" -Duser.home=/opt" \
      && sh mqbroker -n namesrv:9876
     ```
     修改后：
     ```shell
     FROM apache/rocketmq-base:4.3.0
     
     EXPOSE 10909 10911
     
     RUN mv ${ROCKETMQ_HOME}/bin/runbroker-customize.sh ${ROCKETMQ_HOME}/bin/runbroker.sh \
      && chmod +x ${ROCKETMQ_HOME}/bin/runbroker.sh \
      && chmod +x ${ROCKETMQ_HOME}/bin/mqbroker
     
     # 设置配置文件，在配置文件中添加宿主机IP
     RUN echo "brokerIP1=154.8.143.230" > ${ROCKETMQ_HOME}/conf/broker.properties
     
     CMD cd ${ROCKETMQ_HOME}/bin \
      && export JAVA_OPT=" -Duser.home=/opt" \
      # 设置IP和配置文件并设置自动创建Topic
      && sh mqbroker -n 154.8.143.230:9876 -c ../conf/broker.properties autoCreateTopicEnable=true
     ```
   c). 运行`./play-docker-compose.sh`
   
3. 安装控制台参照[文档](https://github.com/apache/rocketmq-externals/blob/d1f387af651e0d7bbe060e1af4dd2b555b3e3e01/rocketmq-console/README.md)

4. 参考文章

    [文章1](https://blog.csdn.net/mefly521/article/details/84394483)
    
    [文章2](https://my.oschina.net/u/3846635/blog/1802448)