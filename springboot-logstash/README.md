## 通过logstash-logback-encoder发送日志到logstash
[logstash-logback-encoder](https://github.com/logstash/logstash-logback-encoder)
logback_to_logstash.conf为logstash配置文件

## 在Spring boot的logback-spring.xml文件中配置如下
```xml
<springProfile name="dev">
    <appender name="async-logstash" class="net.logstash.logback.appender.LoggingEventAsyncDisruptorAppender">
        <appender class="net.logstash.logback.appender.LogstashTcpSocketAppender">
            <!--<destination>10.29.30.214:9250</destination>-->
            <destination>192.168.1.21:9250</destination>

            <!-- encoder is required -->
            <encoder class="net.logstash.logback.encoder.LogstashEncoder" />
        </appender>
    </appender>

    <root level="DEBUG">
        <appender-ref ref="async-logstash" />
        <appender-ref ref="CONSOLE" />
    </root>
</springProfile>
```