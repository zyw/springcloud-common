package cn.v5cn.springcloud.authserver.mapper;

import cn.v5cn.springcloud.authserver.domain.SysClientDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 配置OAuth2的客户端相关信息表 Mapper 接口
 * </p>
 *
 * @author ZYW
 * @since 2018-06-01
 */
@Mapper
public interface SysClientDetailsMapper extends BaseMapper<SysClientDetails> {

}
