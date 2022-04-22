package cn.v5cn.netty.im.rest.web.service.impl;

import cn.v5cn.netty.im.common.domain.po.DbModel;
import cn.v5cn.netty.im.common.domain.po.Offline;
import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.protobuf.constant.MsgTypeEnum;
import cn.v5cn.netty.im.protobuf.generate.Ack;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import cn.v5cn.netty.im.rest.web.mapper.OfflineMapper;
import cn.v5cn.netty.im.rest.web.service.OfflineService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfflineServiceImpl extends ServiceImpl<OfflineMapper, Offline> implements OfflineService {
    @Override
    public void saveChat(Chat.ChatMsg msg) {
        Offline offline = new Offline();
        offline.setMsgId(msg.getId());
        offline.setMsgCode(MsgTypeEnum.CHAT.getCode());
        offline.setToUserId(msg.getDestId());
        offline.setContent(msg.toByteArray());

        saveOffline(offline);
    }

    @Override
    public void saveAck(Ack.AckMsg msg) {
        Offline offline = new Offline();
        offline.setMsgId(msg.getId());
        offline.setMsgCode(MsgTypeEnum.getByClass(Ack.AckMsg.class).getCode());
        offline.setToUserId(msg.getDestId());
        offline.setContent(msg.toByteArray());

        saveOffline(offline);
    }

    private void saveOffline(Offline offline) {
        if(!save(offline)) {
            throw new ImException("[offline] save chat msg failed");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Offline> pollOfflineMsg(String userId) throws JsonProcessingException {
        final List<Offline> unreadList = list(new LambdaQueryWrapper<Offline>().eq(Offline::getToUserId, userId));
        return unreadList.stream().filter(offline -> baseMapper.readMsg(offline.getId()) > 0)
                .sorted(Comparator.comparing(DbModel::getId))
                .collect(Collectors.toList());
    }
}
