package cn.v5cn.sb.netty.common.codec;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * WebSocket专用
 * 请求从TextWebSocketFrame转换成（解码）Invocation对象
 * 响应时从Invocation对象转换成(编码)TextWebSocketFrame对象
 * @author zyw
 */
public class WebSocketFrameInvocationCodec extends MessageToMessageCodec<TextWebSocketFrame,Invocation> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Invocation msg, List<Object> out) throws Exception {
        out.add(new TextWebSocketFrame(JSON.toJSONString(msg)));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        Invocation invocation = JSON.parseObject(msg.text(), Invocation.class);
        out.add(invocation);
    }
}
