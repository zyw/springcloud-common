// 协议试题
export interface Invocation {
    /**
     * 消息类型
     */
    type: string
    /**
     * 消息体
     */
    message: string
}
/**
 * 消息体基础类
 */
 export interface ReqMessage {
    /**
     * 消息编号
     */
    msgId: string
    /**
     * 发送消息的时间戳（纳秒）
     */
    sendTime: number
}
export interface AuthRequest extends ReqMessage {
     /**
      * 用户token
      */
     accessToken: string
}

export interface ChatSendToGroupRequest extends ReqMessage {
    /**
     * 发消息用户
     */
     fromUser: string
     /**
      * 内容
      */
     content: string
     /**
      * 群ID
      */
     groupId: number
}

export interface ChatSendToOneRequest extends ReqMessage {
    /**
     * 发送给的用户
     */
    toUser: string
    /**
     * 来自用户
     */
    fromUser: string
    /**
     * 内容
     */
    content: string
}

export interface RespMessage {
    /**
     * 消息编号
     */
     msgId?: string
    /**
     * 发送消息的时间戳（纳秒）
     */
    sendTime?: number
}

export interface ChatRedirectRequest extends RespMessage {
    /**
     * 发送消息用户
     */
    fromUser?: string
    /**
     * 内容
     */
    content?: string
}

export interface ChatSendResponse extends RespMessage {
    /**
     * 响应状态码
     */
    code?: number
    /**
     * 响应提示
     */
    message?: string
}

export interface HeartbeatResponse extends RespMessage {}

export interface AuthResponse extends RespMessage {
    /**
     * 响应状态码
     */
    code?: number
    /**
     * 响应提示
     */
    message?: string
}

export interface ResponseError extends RespMessage {
    err?: string
}
