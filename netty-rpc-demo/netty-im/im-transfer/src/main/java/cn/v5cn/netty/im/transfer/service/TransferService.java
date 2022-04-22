package cn.v5cn.netty.im.transfer.service;

import cn.v5cn.netty.im.common.domain.conn.Conn;
import cn.v5cn.netty.im.common.domain.conn.ConnectorConn;
import cn.v5cn.netty.im.common.domain.constant.ImConstant;
import cn.v5cn.netty.im.common.domain.constant.MsgVersion;
import cn.v5cn.netty.im.common.util.IdWorker;
import cn.v5cn.netty.im.protobuf.generate.Ack;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import cn.v5cn.netty.im.protobuf.generate.Internal;
import cn.v5cn.netty.im.transfer.domain.ConnectorConnContext;
import cn.v5cn.netty.im.transfer.start.TransferMqProducer;
import cn.v5cn.netty.im.transfer.start.TransferStarter;
import com.google.inject.Inject;
import com.google.protobuf.Message;
import com.rabbitmq.client.MessageProperties;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 * @author yrw
 */
public class TransferService {

    private ConnectorConnContext connContext;
    private TransferMqProducer producer;

    @Inject
    public TransferService(ConnectorConnContext connContext) {
        this.connContext = connContext;
        this.producer = TransferStarter.producer;
    }

    public void doChat(Chat.ChatMsg msg) throws IOException {
        final ConnectorConn conn = connContext.getConnByUserId(msg.getDestId());

        if(conn != null) {
            conn.getCtx().writeAndFlush(msg);
        } else {
            doOffline(msg);
        }
    }

    public void doGreet(Internal.InternalMsg msg, ChannelHandlerContext ctx) {
        ctx.channel().attr(Conn.NET_ID).set(msg.getMsgBody());
        ConnectorConn conn = new ConnectorConn(ctx);
        connContext.addConn(conn);

        ctx.writeAndFlush(getInternalAck(msg.getId()));
    }

    public void doSendAck(Ack.AckMsg msg) throws IOException {
        ConnectorConn conn = connContext.getConnByUserId(msg.getDestId());

        if(conn != null) {
            conn.getCtx().writeAndFlush(msg);
        } else {
            doOffline(msg);
        }

    }

    private Internal.InternalMsg getInternalAck(long msgId) {
        return Internal.InternalMsg.newBuilder()
                .setVersion(MsgVersion.V1.getVersion())
                .setId(IdWorker.snowGenId())
                .setFrom(Internal.InternalMsg.Module.TRANSFER)
                .setDest(Internal.InternalMsg.Module.CONNECTOR)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(Internal.InternalMsg.MsgType.ACK)
                .setMsgBody(msgId + "")
                .build();
    }

    private void doOffline(Message msg) throws IOException {
        producer.basicPublish(ImConstant.MQ_EXCHANGE, ImConstant.MQ_ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg);
    }

}
