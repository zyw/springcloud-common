import { w3cwebsocket, IMessageEvent, ICloseEvent } from "websocket"
import { Invocation, AuthRequest, ReqMessage, ChatSendResponse, RespMessage, AuthResponse, ResponseError, HeartbeatResponse, ChatRedirectRequest } from "./entitys"
import { generateMsgId, nanoTime } from "./util"


const heartbeatInterval = 10 //seconds

export enum MsgType {
    /**
     * 登录请求
     */
    AUTH_REQUEST = "AUTH_REQUEST",
    /**
     * 登录响应
     */
    AUTH_RESPONSE = "AUTH_RESPONSE",

    /**
     * 消息Ack
     */
    CHAT_SEND_RESPONSE = "CHAT_SEND_RESPONSE",

    /**
     * 全体请求
     */
    CHAT_SEND_TO_ALL_REQUEST = "CHAT_SEND_TO_ALL_REQUEST",

    /**
     * 群聊请求
     */
    CHAT_SEND_TO_GROUP_REQUEST = "CHAT_SEND_TO_GROUP_REQUEST",

    /**
     * 单聊请求
     */
    CHAT_SEND_TO_ONE_REQUEST = "CHAT_SEND_TO_ONE_REQUEST",
    /**
     * 转发响应
     */
    CHAT_REDIRECT_TO_USER_REQUEST = "CHAT_REDIRECT_TO_USER_REQUEST",
    /**
     * 心跳请求
    */
    HEARTBEAT_REQUEST = "HEARTBEAT_REQUEST",
    /**
     * 心跳响应
     */
    HEARTBEAT_RESPONSE = "HEARTBEAT_RESPONSE"
}

/**
 * 心跳请求消息
 */
const heartbeatReq: Invocation = {
    type: MsgType.HEARTBEAT_REQUEST,
    message: "",
}

export enum State {
    /**
     * 初始化
     */
    INIT,
    /**
     * 正在连接
     */
    CONNECTING,
    /**
     * 连接中
     */
    CONNECTED,
    /**
     * 正在重连
     */
    RECONNECTING,
    /**
     * 正在关闭
     */
    CLOSEING,
    /**
     * 已关闭
     */
    CLOSED,
}

export enum Ack {
    Success = "Success",
    Timeout = "Timeout",
    Loginfailed = "LoginFailed",
    Logined = "Logined",
}

export let sleep = async (seconds: number): Promise<void> => {
    return new Promise((resolve, _) => {
        setTimeout(() => {
            resolve()
        }, seconds * 1000)
    })
}

// 打开WebSocket连接
export let doConnect = async (url: string): Promise<{ status: string, conn: w3cwebsocket }> => {
    const LoginTimeout = 5  // 5 seconds
    return new Promise((resolve, reject) => {
        let conn = new w3cwebsocket(url)
        conn.binaryType = "arraybuffer"

        // 设置一个登陆超时器
        let tr = setTimeout(() => {
            resolve({status: Ack.Timeout, conn: conn});
        }, LoginTimeout * 1000);

        // 打开连接成功
        conn.onopen = () => {
            console.info("websocket open - readyState:", conn.readyState)

            if (conn.readyState === w3cwebsocket.OPEN) {
                clearTimeout(tr)
                resolve({status: Ack.Success, conn: conn})
            }
        }

        // 打开连接错误
        conn.onerror = (error: Error) => {
            clearTimeout(tr)
            console.debug(error)
            resolve({status: Ack.Loginfailed, conn: conn})
        }
    })
}

// 连接关闭回调
export interface ConnectCloseCallback {
    callback(conn: w3cwebsocket | null):void;
}

// 响应消息回调
export interface RespMsgCallback {
    respMsg(msg: RespMessage):void;
}

export class IMClient {
    wsurl: string
    token: string
    state = State.INIT
    private conn: w3cwebsocket | null
    private lastRead: number
    private callback?: ConnectCloseCallback
    private respCallback?: RespMsgCallback

    constructor(url: string, token: string, callbackFun?: ConnectCloseCallback) {
        this.wsurl = url
        this.token = token
        this.conn = null
        this.callback = callbackFun
        this.lastRead = Date.now()
    }

    // 1. 验证token,注册用户
    async checkToken(respMsgCallback?: RespMsgCallback): Promise<{ status: string }> {

        this.respCallback = respMsgCallback

        if (this.state === State.CONNECTED) {
            return { status: Ack.Logined }
        }
        this.state = State.CONNECTING

        let { status, conn } = await doConnect(this.wsurl)
        console.info("login - ", status)

        if (status !== Ack.Success) {
            this.state = State.INIT
            return { status }
        }

        let authReq: AuthRequest = {
            msgId: generateMsgId(),
            accessToken: this.token,
            sendTime: nanoTime()
        }

        let authMsg: Invocation = {
            type: MsgType.AUTH_REQUEST,
            message: JSON.stringify(authReq)
        }

        // overwrite onmessage
        conn.onmessage = (evt: IMessageEvent) => {
            this.onMessage(evt, this.respCallback)
        }

        conn.onerror = (error) => {
            console.info("websocket error: ", error)
            this.errorHandler(error)
        }

        conn.onclose = (e: ICloseEvent) => {
            console.debug("event[onclose] fired")
            if (this.state === State.CLOSEING) {
                this.onclose("logout")
                return
            }
            this.errorHandler(new Error(e.reason))
        }
        this.conn = conn
        this.state = State.CONNECTED

        if(!this.send(JSON.stringify(authMsg))) {
            
            console.warn("发送消息失败！")
        }

        return { status }
    }
    // 发送消息
    sendMessage(type: string, msg: ReqMessage) {
        let payload: Invocation = {
            type: type,
            message: JSON.stringify(msg)
        }

        let res = this.send(JSON.stringify(payload))
        if (!res) {
            this.onclose("发送消息失败")
        }
    }
    // 退出
    logout() {
        if (this.state === State.CLOSEING) {
            return
        }
        this.state = State.CLOSEING
        if(!this.conn) {
            return
        }
        console.info("Connection closing...")
        this.conn.close()
    }

    private onMessage(evt: IMessageEvent, respCallback?: RespMsgCallback) {
        try {
            this.lastRead = Date.now()

            // console.info("response: ", evt)

            let buf = Buffer.from(<ArrayBuffer>evt.data)

            let respMsg = JSON.parse(buf.toString())
            let msg: any;

            switch (respMsg.type) {
                case MsgType.AUTH_RESPONSE:
                    msg = JSON.parse(respMsg.message)
                    if(msg.code === 0) {
                        // 开启心跳
                        this.heartbeatLoop()
                        // 开启读超时
                        this.readDeadlineLoop()
                    } else {
                        this.onclose("token验证失败")
                    }
    
                    let authResp: AuthResponse = {
                        msgId: msg.msgId,
                        sendTime: msg.sendTime,
                        code: msg.code,
                        message: msg.message
                    }
    
                    respCallback?.respMsg(authResp)
                    console.info(`response type: ${ respMsg.type } - response code: ${ msg.code } - response message: ${ msg.message }`)
                    break;
                case MsgType.HEARTBEAT_RESPONSE:
                    let heartbeat: HeartbeatResponse = {}
                    respCallback?.respMsg(heartbeat)
                    break;
                case MsgType.CHAT_SEND_RESPONSE:
                    msg = JSON.parse(respMsg.message)
                    let chatSendResp: ChatSendResponse = {
                        msgId: msg.msgId,
                        sendTime: msg.sendTime,
                        code: msg.code,
                        message: msg.message
                    }
                    respCallback?.respMsg(chatSendResp)
                    break;
                case MsgType.CHAT_REDIRECT_TO_USER_REQUEST:
                    msg = JSON.parse(respMsg.message)
                    let chatResp: ChatRedirectRequest = {
                        msgId: msg.msgId,
                        sendTime: msg.sendTime,
                        fromUser: msg.fromUser,
                        content: msg.content
                    }
                    respCallback?.respMsg(chatResp)
                    break;
                default:
                    console.warn(`response type: ${ respMsg.type } - response message: ${ respMsg.message }`)

                    let err: ResponseError = {
                        err: "没有对映的消息类型：" + respMsg.type
                    }
                    respCallback?.respMsg(err)
                    break;
            }

        } catch (error) {
            console.error(evt.data, error)
        }
    }

    // 2. 心跳
    private heartbeatLoop() {
        console.debug("heartbeatLoop start...")

        let loop = () => {
            if(this.state != State.CONNECTED) {
                console.debug("heartbeatLoop exited")
                return
            }

            console.log(`>>> send ping; state is ${this.state}`)

            this.send(JSON.stringify(heartbeatReq))

            setTimeout(loop, heartbeatInterval * 1000)
        }
        setTimeout(loop, heartbeatInterval * 1000)
    }
    // 3. 读超时
    private readDeadlineLoop() {
        console.debug("deadlineLoop start...")
        let loop = () => {
            if(this.state != State.CONNECTED) {
                console.debug("deadlineLoop exited...")
                return
            }
            if ((Date.now() - this.lastRead) > 3 * heartbeatInterval * 1000) {
                // 如果超时就调用errorHandler处理
                this.errorHandler(new Error("read timeout"))
            }
            setTimeout(loop, 1000)
        }
        setTimeout(loop, 1000)
    }
    // 表示连接终止
    private onclose(reason: string) {
        console.info("connection closed due to " + reason)
        this.state = State.CLOSED
        // 通知上层应用，这里忽略
        this.closeCallback()

        if(!this.conn) {
            return
        }
        console.info("Connection closing...")
        this.conn.close()
    }
    // 4. 自动重连
    private async errorHandler(error: Error) {
        // 如果是主动断开连接，就没有必要自动重连
        // 比如收到被踢，或者主动调用logout()方法
        if (this.state === State.CLOSED || this.state === State.CLOSEING) {
            return
        }
        this.state = State.RECONNECTING
        console.debug(error)
        // 重连10次
        for (let index = 0; index < 10; index++) {
            try {
                console.info("try to relogin")
                let { status } = await this.checkToken(this.respCallback)
                if (status == Ack.Success) {
                    return
                }
            } catch (error) {
                console.warn(error)
            }
            // 重连间隔时间，演示使用固定值
            await sleep(5)
        }
        this.onclose("reconnect timeout")
    }

    // 连接关闭回调函数
    private closeCallback() {
        this.callback?.callback(this.conn)
    }

    private send(data: string): boolean {
        try {
            if (this.conn == null) {
                return false
            }
            this.conn.send(data)
        } catch (error) {
            // handler write error
            this.errorHandler(new Error("read timeout"))
            return false
        }
        return true
    }
}