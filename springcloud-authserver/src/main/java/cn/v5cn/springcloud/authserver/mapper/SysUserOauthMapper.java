package cn.v5cn.springcloud.authserver.mapper;

import cn.v5cn.springcloud.authserver.domain.SysUser;
import cn.v5cn.springcloud.authserver.dto.AuthMarkingDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ZYW
 * @since 2018-05-03
 */
@Mapper
public interface SysUserOauthMapper extends BaseMapper<SysUser> {

    List<AuthMarkingDTO> findAuthMarkingByUserId(@Param("userId") Long userId);

}
