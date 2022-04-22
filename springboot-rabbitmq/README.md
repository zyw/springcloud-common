## RabbitMQ 例子

### 1、交换机（Exchange）

交换机为`FanoutExchange`模式

### 2、延时队列
延时队列使用`rabbitmq-delayed-message-exchange`插件来完成延时队列的创建。
插件[Github](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/)

### 参考文章
1. [www.pandan.xyz/categories/rabbitmq/](www.pandan.xyz/categories/rabbitmq/)
2. [https://segmentfault.com/a/1190000016072908](https://segmentfault.com/a/1190000016072908)

### Docker安装RabbitMQ
[https://hub.docker.com/_/rabbitmq/](https://hub.docker.com/_/rabbitmq/)
```
docker run -d --name rabbitmq -p 15672:15672 -p 5672:5672 -e RABBITMQ_DEFAULT_USER=zhangsan -e RABBITMQ_DEFAULT_PASS=000000 rabbitmq:3.7.8-management
```

### 保证消息的有序性
1、[https://segmentfault.com/a/1190000014512075](https://segmentfault.com/a/1190000014512075)
2、在分布式系统中，要想消息有顺序的被消费，无论是Activemq还是Rocketmq都要想办法让有顺序的消息被同一消费者消费，
    而不是并发的消费，在消费者消费成功后，接着才会消费下一个消息，这样就可以保证了严格的顺序。