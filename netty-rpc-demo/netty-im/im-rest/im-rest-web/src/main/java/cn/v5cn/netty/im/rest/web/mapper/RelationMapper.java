package cn.v5cn.netty.im.rest.web.mapper;

import cn.v5cn.netty.im.common.domain.po.Relation;
import cn.v5cn.netty.im.common.domain.po.RelationDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RelationMapper extends BaseMapper<Relation> {
    /**
     * list user's friends
     *
     * @param userId
     * @return
     */
    List<RelationDetail> listFriends(@Param("userId") String userId);
}
