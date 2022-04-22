package cn.v5cn.netty.im.rest.web.mapper;

import cn.v5cn.netty.im.common.domain.po.Offline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface OfflineMapper extends BaseMapper<Offline> {

    /**
     * read offline msg from db, cas
     * @param msgId
     * @return
     */
    int readMsg(Long msgId);
}
