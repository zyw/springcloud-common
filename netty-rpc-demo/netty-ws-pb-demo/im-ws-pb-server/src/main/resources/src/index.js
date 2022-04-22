// var ack = require('./ack_pb');
// var chat = require('./chat_pb');
// var ByteBuffer = require("byte-buffer");

import ack from './ack_pb';
import chat from './chat_pb';

import ByteBuffer from 'byte-buffer';

function stringToUint(string) {
    // var string = btoa(unescape(encodeURIComponent(string))),
    //     charList = string.split(''),
    //     uintArray = [];
    // for (var i = 0; i < charList.length; i++) {
    //     uintArray.push(charList[i].charCodeAt(0));
    // }
    // return new Uint8Array(uintArray);

    return new TextEncoder().encode(string);
}

function uintToString(uintArray) {
    // var encodedString = String.fromCharCode.apply(null, uintArray),
    //     decodedString = decodeURIComponent(escape(atob(encodedString)));
    // return decodedString;
    return new TextDecoder().decode(uintArray);
}

var chatMsg = new chat.ChatMsg();
chatMsg.setVersion(1);
chatMsg.setId(123);
chatMsg.setDesttype(chat.ChatMsg.DestType.SINGLE);
chatMsg.setFromid("123");
chatMsg.setDestid("254");
chatMsg.setCreatetime(12457);
chatMsg.setMsgtype(chat.ChatMsg.MsgType.TEXT);
chatMsg.setMsgbody(stringToUint("你好"));

// 通过Protobuf序列化实体
var bytes = chatMsg.serializeBinary();

var socket;
window.connect = function() {
    socket = new WebSocket("ws://127.0.0.1:8888/im");
    socket.onopen = function(e){
        console.log("打开WebSocket连接回调：",e);
    };
    socket.onerror = function(e){
        console.log("连接错误异常回调：", e);
    };
    socket.onclose = function(e){
        console.log("WebSocket关闭回调", e);
    };
    socket.onmessage = async function(e){

        var a = await e.data.arrayBuffer();

        var b = new ByteBuffer(a);

        console.log("---------------", b)

        var len = b.readInt()
        var code = b.readInt()

        console.log("======================" + len + "---------------" + code)
        

        var message2 = chat.ChatMsg.deserializeBinary(b.read().buffer);

        console.log(message2.getVersion() + "--------------" + message2.getFromid() + "=================" + message2.getDestid() + " msg: " + uintToString(message2.getMsgbody()))
    };
}

// 发送消息到WebSocket服务
window.send = function() {

    var b = new ByteBuffer(bytes.length + 8);
    b.writeInt(bytes.length);
    b.writeInt(1);
    b.write(bytes);

    socket.send(b.buffer);
}

console.log("=========" + bytes.length)
var message2 = chat.ChatMsg.deserializeBinary(bytes);
console.log(message2.getVersion() + "--------------" + message2.getFromid() + "=================" + message2.getDestid() + " msg: " + uintToString(message2.getMsgbody()))