package cn.v5cn.sb.netty.common.codec;

import cn.v5cn.sb.netty.common.dispacher.Message;
import com.alibaba.fastjson.JSON;

/**
 * 通信协议的消息体
 * @author 艿艿
 */
public class Invocation {
    /**
     * 类型
     */
    private String type;
    /**
     * 消息，JSON 格式
     */
    private String message;

    /**
     * 空构造方法
     */
    public Invocation() {
    }

    public Invocation(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Invocation(String type, Message message) {
        this.type = type;
        this.message = JSON.toJSONString(message);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
