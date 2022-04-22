package cn.v5cn.netty.protobuf.demo.handler;

import cn.v5cn.netty.protobuf.demo.entity.Student;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author powertime
 */
public class StudentServerHandler extends SimpleChannelInboundHandler<Student> {

    private final static Logger LOGGER = LoggerFactory.getLogger(StudentServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Student student) throws Exception {
        LOGGER.info("server收到消息{}",student);
        // 写入消息
        ChannelFuture future = ctx.write(student);
    }
}
