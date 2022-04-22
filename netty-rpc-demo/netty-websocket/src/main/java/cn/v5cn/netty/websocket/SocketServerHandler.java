package cn.v5cn.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * 打了@Deprecated的类是一套工程。
 */
@Deprecated
public class SocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 打印接受到的消息
        System.out.println("服务端接受到消息： " + textWebSocketFrame.text());
        //返回消息给客户端
        channelHandlerContext.writeAndFlush(new TextWebSocketFrame("服务器：" + LocalDateTime.now() + " : " + textWebSocketFrame.text()));
    }

    /**
     * 客户端连接时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // LongText()唯一的 ShortText()不唯一
        System.out.println("handleAdded : " + ctx.channel().id().asLongText());
        System.out.println("handleAdded : " + ctx.channel().id().asShortText());
    }

    /**
     * 客户端断开时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生了。。。。。。");
        ctx.close();
    }
}
