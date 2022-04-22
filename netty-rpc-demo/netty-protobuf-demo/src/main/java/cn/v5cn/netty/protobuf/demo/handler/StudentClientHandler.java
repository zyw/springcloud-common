package cn.v5cn.netty.protobuf.demo.handler;

import cn.v5cn.netty.protobuf.demo.entity.Student;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author powertime
 */
public class StudentClientHandler extends SimpleChannelInboundHandler<Student> {

    private final static Logger LOGGER = LoggerFactory.getLogger(StudentClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Student student) throws Exception {
        LOGGER.info("client收到消息{}",student);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // channel活跃
        // 构建一个Student,并将其写入到channel中
        Student student = Student.newBuilder().setAge(22).setName("flydean").build();
        LOGGER.info("client发送消息{}", student);
        ctx.write(student);
        ctx.flush();
    }
}
