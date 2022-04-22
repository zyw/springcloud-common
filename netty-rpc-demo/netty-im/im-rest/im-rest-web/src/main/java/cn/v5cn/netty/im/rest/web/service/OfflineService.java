package cn.v5cn.netty.im.rest.web.service;

import cn.v5cn.netty.im.common.domain.po.Offline;
import cn.v5cn.netty.im.protobuf.generate.Ack;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface OfflineService extends IService<Offline> {
    /**
     * save offline chat msg
     *
     * @param msg
     * @return
     */
    void saveChat(Chat.ChatMsg msg);

    /**
     * save offline ack msg
     *
     * @param msg
     * @return
     */
    void saveAck(Ack.AckMsg msg);

    /**
     * get a user's all offline msgs
     *
     * @param userId
     * @return
     * @throws JsonProcessingException
     */
    List<Offline> pollOfflineMsg(String userId) throws JsonProcessingException;
}
