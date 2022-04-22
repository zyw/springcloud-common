package cn.v5cn.netty.im.rest.web.service.impl;

import cn.v5cn.netty.im.common.domain.po.Relation;
import cn.v5cn.netty.im.common.domain.po.RelationDetail;
import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.rest.spi.UserSpi;
import cn.v5cn.netty.im.rest.spi.domain.UserBase;
import cn.v5cn.netty.im.rest.web.mapper.RelationMapper;
import cn.v5cn.netty.im.rest.web.service.RelationService;
import cn.v5cn.netty.im.rest.web.spi.SpiFactory;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation> implements RelationService {

    private UserSpi<? extends UserBase> userSpi;

    public RelationServiceImpl(SpiFactory spiFactory) {
        this.userSpi = spiFactory.getUserSpi();
    }

    @Override
    public List<RelationDetail> friends(String id) {
        return baseMapper.listFriends(id);
    }

    @Override
    public Long saveRelation(String userId1, String userId2) {
        if(userId1.equals(userId2)) {
            throw new ImException("[rest] userId1 and userId2 can not be same");
        }
        if(userSpi.getById(userId1 + "") == null || userSpi.getById(userId2 + "") == null) {
            throw new ImException("[rest] user not exist");
        }
        final String max = userId1.compareTo(userId2) >= 0 ? userId1 : userId2;
        final String min = max.equals(userId1) ? userId2 : userId1;

        Relation relation = new Relation();
        relation.setUserId1(min);
        relation.setUserId2(max);
        relation.setEncryptKey(RandomStringUtils.randomAlphanumeric(16) + "|" + RandomStringUtils.randomNumeric(16));

        if(save(relation)) {
            return relation.getId();
        }
        throw new ImException("[rest] save relation failed");
    }
}
