package cn.v5cn.netty.ws.pb.server.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.v5cn.netty.ws.pb.core.constant.MsgVersion;
import cn.v5cn.netty.ws.pb.core.entity.AckMsg;
import cn.v5cn.netty.ws.pb.core.entity.ChatMsg;
import cn.v5cn.netty.ws.pb.core.exception.ImLogicException;
import cn.v5cn.netty.ws.pb.core.handler.ServerAckHandler;
import cn.v5cn.netty.ws.pb.core.util.IdWorker;
import cn.v5cn.netty.ws.pb.server.domain.ClientConn;
import cn.v5cn.netty.ws.pb.server.domain.ClientConnContext;
import com.google.inject.Inject;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * @author ZYW
 */
public class ServerToClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerToClientService.class);

    private final ClientConnContext clientConnContext;

    @Inject
    public ServerToClientService(ClientConnContext clientConnContext) {
        this.clientConnContext = clientConnContext;
    }

    public void doChatToClientAndFlush(ChatMsg msg) {
        ClientConn conn = clientConnContext.getConnByUserId(msg.getDestId());
        if(conn == null) {
            //todo: if not on the machine
            LOGGER.error("[send chat to client] not one the machine, userId: {}, connectorId: {}",
                    msg.getDestId(), msg.getFromId());
            return;
        }
        ChatMsg copy = ChatMsg
                .newBuilder()
                .mergeFrom(msg)
                .setId(IdWorker.nextId(conn.getNetId()))
                .build();
        conn.getCtx().writeAndFlush(copy);
        //send delivered
        sendMsg(msg.getFromId(), msg.getId(), cid -> getDelivered(cid, msg));
    }

    public void doChatGroupToClientAndFlush(ChatMsg msg){

        List<ClientConn> connList = clientConnContext.getConnListByGroupId(msg.getDestId());

        if(CollectionUtil.isEmpty(connList)) {
            //todo: if not on the machine
            LOGGER.error("[send chat to client] not one the machine, userId: {}, connectorId: {}",
                    msg.getDestId(), msg.getFromId());
            return;
        }

        connList.forEach(item -> {
            ChatMsg copy = ChatMsg
                    .newBuilder()
                    .mergeFrom(msg)
                    .setId(IdWorker.nextId(item.getNetId()))
                    .build();
            item.getCtx().writeAndFlush(copy);
        });
        //send delivered
        sendMsg(msg.getFromId(), msg.getId(), cid -> getDelivered(cid, msg));
    }

    public void doSendAckToClientAndFlush(AckMsg msg) {
        final ClientConn conn = clientConnContext.getConnByUserId(msg.getDestId());
        if(conn == null) {
            //todo: if not on the machine
            LOGGER.error("[send msg to client] not one the machine, userId: {}, connectorId: {}",
                    msg.getDestId(), msg.getFromId());
            return;
        }

        // change msg id
        AckMsg copy = AckMsg
                .newBuilder()
                .mergeFrom(msg)
                .setId(IdWorker.nextId(conn.getNetId()))
                .build();

        conn.getCtx().writeAndFlush(copy);
    }

    private AckMsg getDelivered(Serializable connectionId, ChatMsg msg) {
        return AckMsg.newBuilder()
                .setId(IdWorker.nextId(connectionId))
                .setVersion(MsgVersion.V1.getVersion())
                .setFromId(msg.getDestId())
                .setDestId(msg.getFromId())
                .setDestType(msg.getDestType() == ChatMsg.DestType.SINGLE ? AckMsg.DestType.SINGLE : AckMsg.DestType.GROUP)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(AckMsg.MsgType.DELIVERED)
                .setAckMsgId(msg.getId())
                .build();
    }

    private boolean sendMsg(String destId, Long msgId, Function<Serializable, Message> generateMsg) {
        ClientConn conn = clientConnContext.getConnByUserId(destId);

        if(conn == null) {
            throw new ImLogicException("没有找到对应的连接！");
        }

        //the user is connected to this machine
        //won 't save chat histories
        final Message msg = generateMsg.apply(conn.getNetId());

        ServerAckHandler.offer(conn.getNetId(), msgId, msg, m -> conn.getCtx().writeAndFlush(m));

        return true;
    }

}
