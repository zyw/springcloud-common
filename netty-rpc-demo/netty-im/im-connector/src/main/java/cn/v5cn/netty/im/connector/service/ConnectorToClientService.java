package cn.v5cn.netty.im.connector.service;

import cn.v5cn.netty.im.common.domain.ack.ServerAckWindow;
import cn.v5cn.netty.im.common.domain.conn.Conn;
import cn.v5cn.netty.im.common.domain.constant.MsgVersion;
import cn.v5cn.netty.im.common.util.IdWorker;
import cn.v5cn.netty.im.connector.domain.ClientConn;
import cn.v5cn.netty.im.connector.domain.ClientConnContext;
import cn.v5cn.netty.im.connector.handler.ConnectorTransferHandler;
import cn.v5cn.netty.im.protobuf.generate.Ack;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import com.google.inject.Inject;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author yrw
 */
public class ConnectorToClientService {

    private static final Logger logger = LoggerFactory.getLogger(ConnectorToClientService.class);

    private final ClientConnContext clientConnContext;

    @Inject
    public ConnectorToClientService(ClientConnContext clientConnContext) {
        this.clientConnContext = clientConnContext;
    }

    public void doChatToClientAndFlush(Chat.ChatMsg msg) {
        Conn conn = clientConnContext.getConnByUserId(msg.getDestId());
        if (conn == null) {
            //todo: if not on the machine
            logger.error("[send chat to client] not one the machine, userId: {}, connectorId: {}",
                    msg.getDestId(), ConnectorTransferHandler.CONNECTOR_ID);
            return;
        }
        //change msg id
        Chat.ChatMsg copy = Chat.ChatMsg
                .newBuilder()
                .mergeFrom(msg)
                .setId(IdWorker.nextId(conn.getNetId()))
                .build();

        conn.getCtx().writeAndFlush(copy);
        //send delivered
        sendMsg(msg.getFromId(), msg.getId(), cid -> getDelivered(cid, msg));
    }

    public void doSendAckToClientAndFlush(Ack.AckMsg ackMsg) {
        final ClientConn conn = clientConnContext.getConnByUserId(ackMsg.getDestId());
        if(conn == null) {
            //todo: if not on the machine
            logger.error("[send msg to client] not one the machine, userId: {}, connectorId: {}",
                    ackMsg.getDestId(), ConnectorTransferHandler.CONNECTOR_ID);
            return;
        }
        // change msg id
        Ack.AckMsg copy = Ack.AckMsg
                .newBuilder()
                .mergeFrom(ackMsg)
                .setId(IdWorker.nextId(conn.getNetId()))
                .build();
        conn.getCtx().writeAndFlush(copy);
    }

    public void doChatToClientOrTransferAndFlush(Chat.ChatMsg chat) {
        final boolean onTheMachine = sendMsg(chat.getDestId(), chat.getId(),
                cid -> Chat.ChatMsg.newBuilder().mergeFrom(chat).setId(IdWorker.nextId(cid)).build());

        // send ack to from id
        if(onTheMachine) {
            final ClientConn conn = clientConnContext.getConnByUserId(chat.getFromId());
            if(conn == null) {
                final ChannelHandlerContext ctx = ConnectorTransferHandler.getOneOfTransferCtx(System.currentTimeMillis());
                ctx.writeAndFlush(getDelivered(ctx.channel().attr(Conn.NET_ID).get(), chat));
            } else {
                // need wait for ack
                Ack.AckMsg delivered = getDelivered(conn.getNetId(), chat);
                ServerAckWindow.offer(conn.getUserId(), delivered.getId(), delivered, m -> conn.getCtx().writeAndFlush(m));
            }
        }
    }

    public void doSendAckToClientOrTransferAndFlush(Ack.AckMsg ackMsg) {
        sendMsg(ackMsg.getDestId(), ackMsg.getId(),
                cid -> Ack.AckMsg.newBuilder().mergeFrom(ackMsg).setId(IdWorker.nextId(cid)).build());
    }

    private Ack.AckMsg getDelivered(Serializable connectionId, Chat.ChatMsg msg) {
        return Ack.AckMsg.newBuilder()
                .setId(IdWorker.nextId(connectionId))
                .setVersion(MsgVersion.V1.getVersion())
                .setFromId(msg.getDestId())
                .setDestId(msg.getFromId())
                .setDestType(msg.getDestType() == Chat.ChatMsg.DestType.SINGLE ? Ack.AckMsg.DestType.SINGLE : Ack.AckMsg.DestType.GROUP)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(Ack.AckMsg.MsgType.DELIVERED)
                .setAckMsgId(msg.getId())
                .build();
    }

    private boolean sendMsg(String destId, Long msgId, Function<Serializable, Message> generateMsg) {
        final ClientConn conn = clientConnContext.getConnByUserId(destId);
        if(conn == null) {
            final ChannelHandlerContext ctx = ConnectorTransferHandler.getOneOfTransferCtx(System.currentTimeMillis());
            ctx.writeAndFlush(generateMsg.apply(ctx.channel().attr(Conn.NET_ID).get()));
            return false;
        } else {
            //the user is connected to this machine
            //won 't save chat histories
            final Message msg = generateMsg.apply(conn.getNetId());
            ServerAckWindow.offer(conn.getNetId(),msgId, msg, m -> conn.getCtx().writeAndFlush(m));
            return true;
        }
    }
}
