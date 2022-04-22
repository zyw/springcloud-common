package cn.v5cn.cim.server.handle;

import cn.v5cn.cim.common.constant.Constants;
import cn.v5cn.cim.common.exception.CIMException;
import cn.v5cn.cim.common.pojo.CIMUserInfo;
import cn.v5cn.cim.common.protocol.CIMRequestProto;
import cn.v5cn.cim.common.util.NettyAttrUtil;
import cn.v5cn.cim.server.kit.RouteHandler;
import cn.v5cn.cim.server.kit.ServerHeartBeatHandlerImpl;
import cn.v5cn.cim.server.util.SessionSocketHolder;
import cn.v5cn.cim.server.util.SpringBeanFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author crossoverJie
 */
@ChannelHandler.Sharable
public class CIMServerHandle extends SimpleChannelInboundHandler<CIMRequestProto.CIMReqProtocol> {
    private final static Logger LOGGER = LoggerFactory.getLogger(CIMServerHandle.class);

    /**
     * 取消绑定
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //可能出现业务判断离线后再次触发 channelInactive
        CIMUserInfo userInfo = SessionSocketHolder.getUserId((NioSocketChannel) ctx.channel());
        if (userInfo != null) {
            LOGGER.warn("[{}] trigger channelInactive offline!", userInfo.getUserName());

            // Clear route info and offline.
            RouteHandler routeHandler = SpringBeanFactory.getBean(RouteHandler.class);
            routeHandler.userOffLine(userInfo, (NioSocketChannel)ctx.channel());

            ctx.channel().close();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CIMRequestProto.CIMReqProtocol msg) throws Exception {
        LOGGER.info("received msg={}",msg.toString());

        if(msg.getType() == Constants.CommandType.LOGIN) {
            // 保存客户端与Channel之间的关系
            SessionSocketHolder.put(msg.getRequestId(), (NioSocketChannel) ctx.channel());
            SessionSocketHolder.saveSession(msg.getRequestId(),msg.getReqMsg());
            LOGGER.info("client [{}] online success!!", msg.getReqMsg());
        }

        // 心跳更新时间
        if(msg.getType() == Constants.CommandType.PING) {
            NettyAttrUtil.updateReaderTime(ctx.channel(), System.currentTimeMillis());
            //问客户端响应pong消息
            CIMRequestProto.CIMReqProtocol heartBeat = SpringBeanFactory.getBean("heartBeat", CIMRequestProto.CIMReqProtocol.class);
            ctx.writeAndFlush(heartBeat).addListener((ChannelFutureListener)future -> {
                if (!future.isSuccess()) {
                    LOGGER.error("IO error,close Channel");
                    future.channel().close();
                }
            });
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state() == IdleState.READER_IDLE) {

                LOGGER.info("定时检测客户端端是否存活");

                ServerHeartBeatHandlerImpl heartBeatHandler = SpringBeanFactory.getBean(ServerHeartBeatHandlerImpl.class);
                heartBeatHandler.process(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(CIMException.isResetByPeer(cause.getMessage())) {
            return;
        }

        LOGGER.error(cause.getMessage(), cause);
    }
}
