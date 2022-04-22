import { w3cwebsocket } from "websocket";
import { ChatSendToOneRequest, RespMessage } from "./entitys";
import { IMClient, sleep, ConnectCloseCallback, MsgType, RespMsgCallback } from "./sdk";
import { generateMsgId, nanoTime } from "./util";

// 测试关闭回调
class ConnCloseCallback implements ConnectCloseCallback {
    callback(conn: w3cwebsocket | null): void {
        console.log("00000000000000000000000000000000000000000000000000000000000000000",conn?.url)
    }
}
// 测试消息响应回调
class RespMsgCallbackImpl implements RespMsgCallback {

    respMsg(msg: RespMessage): void {
        console.log("----------------------------------------------------")
        console.log(msg)
        console.log("====================================================")
    }
    
}

const main = async () => {
    let cli = new IMClient("ws://localhost:8898/im", "09ff8dfc-4168-41c9-9247-eb650e8eafff", new ConnCloseCallback());
    let { status } = await cli.checkToken(new RespMsgCallbackImpl())
    console.log("client login return -- ", status)

    // await sleep(15)

    let toUser: ChatSendToOneRequest = {
        msgId: generateMsgId(),
        toUser: "1440843654259314690",
        fromUser: "2",
        content: "你好",
        sendTime: nanoTime()
    }

    cli.sendMessage(MsgType.CHAT_SEND_TO_ONE_REQUEST, toUser)

    await sleep(15)
    //cli.logout()
}

main();