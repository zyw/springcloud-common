## 修改原型apache的代码来满足发送延迟消息的需求

### 1、修改RocketMQMessageConst类
```java
package org.apache.rocketmq.spring.starter.supports;

public class RocketMQMessageConst {
    public static final String KEYS = "KEYS";
    public static final String TAGS = "TAGS";
    public static final String TOPIC = "TOPIC";
    public static final String MESSAGE_ID = "MESSAGE_ID";
    public static final String BORN_TIMESTAMP = "BORN_TIMESTAMP";
    public static final String BORN_HOST = "BORN_HOST";
    public static final String FLAG = "FLAG";
    public static final String QUEUE_ID = "QUEUE_ID";
    public static final String SYS_FLAG = "SYS_FLAG";
    public static final String TRANSACTION_ID = "TRANSACTION_ID";
    public static final String PROPERTIES = "PROPERTIES";

    /**
     * 设置延时级别(添加一个常量)
     */
    public static final String DELAY_TIME_LEVEL = "DELAY_TIME_LEVEL";
}
```

### 2、修改org.apache.rocketmq.spring.starter.supports.RocketMQUtil类
```java
/**
 * Convert spring message to rocketMQ message
 *
 * @param destination formats: `topicName:tags`
 * @param message     {@link org.springframework.messaging.Message}
 * @return instance of {@link org.apache.rocketmq.common.message.Message}
 */
public static org.apache.rocketmq.common.message.Message convertToRocketMsg(
    ObjectMapper objectMapper, String charset,
    String destination, org.springframework.messaging.Message<?> message) {
    Object payloadObj = message.getPayload();
    byte[] payloads;

    if (payloadObj instanceof String) {
        payloads = ((String) payloadObj).getBytes(Charset.forName(charset));
    } else {
        try {
            String jsonObj = objectMapper.writeValueAsString(payloadObj);
            payloads = jsonObj.getBytes(Charset.forName(charset));
        } catch (Exception e) {
            throw new RuntimeException("convert to RocketMQ message failed.", e);
        }
    }

    String[] tempArr = destination.split(":", 2);
    String topic = tempArr[0];
    String tags = "";
    if (tempArr.length > 1) {
        tags = tempArr[1];
    }

    org.apache.rocketmq.common.message.Message rocketMsg = new org.apache.rocketmq.common.message.Message(topic, tags, payloads);

    MessageHeaders headers = message.getHeaders();
    if (Objects.nonNull(headers) && !headers.isEmpty()) {
        Object keys = headers.get(RocketMQMessageConst.KEYS);
        if (!StringUtils.isEmpty(keys)) { // if headers has 'KEYS', set rocketMQ message key
            rocketMsg.setKeys(keys.toString());
        }

        // set rocketMQ message flag
        Object flagObj = headers.getOrDefault("FLAG", "0");
        int flag = 0;
        try {
            flag = Integer.parseInt(flagObj.toString());
        } catch (NumberFormatException e) {
            // ignore
            log.info("flag must be integer, flagObj:{}", flagObj);
        }
        rocketMsg.setFlag(flag);

        // set rocketMQ message waitStoreMsgOkObj
        Object waitStoreMsgOkObj = headers.getOrDefault("WAIT_STORE_MSG_OK", "true");
        boolean waitStoreMsgOK = Boolean.TRUE.equals(waitStoreMsgOkObj);
        rocketMsg.setWaitStoreMsgOK(waitStoreMsgOK);

        Object delayObj = headers.get(RocketMQMessageConst.DELAY_TIME_LEVEL);
        if (delayObj != null) {
            String delay = String.valueOf(delayObj);
            if(org.apache.commons.lang3.StringUtils.isNumeric(delay)) {
                rocketMsg.setDelayTimeLevel(Integer.valueOf(delay));
            } else {
                log.warn("DELAY_TIME_LEVEL not is num!");
            }
        }

        headers.entrySet().stream()
            .filter(entry -> !Objects.equals(entry.getKey(), RocketMQMessageConst.KEYS)
                && !Objects.equals(entry.getKey(), "FLAG")
                && !Objects.equals(entry.getKey(), "WAIT_STORE_MSG_OK")) // exclude "KEYS", "FLAG", "WAIT_STORE_MSG_OK"
            .forEach(entry -> {
                rocketMsg.putUserProperty("USERS_" + entry.getKey(), String.valueOf(entry.getValue())); // add other properties with prefix "USERS_"
            });

    }

    return rocketMsg;
}
```