package cn.v5cn.springcloud.authserver.security;

import cn.v5cn.springcloud.authserver.domain.SysUser;
import cn.v5cn.springcloud.authserver.exception.CustomAuthenticationException;
import cn.v5cn.springcloud.authserver.exception.ErrorCode;
import cn.v5cn.springcloud.authserver.mapper.SysUserOauthMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SysUserOauthMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password;
        Map data = (Map) authentication.getDetails();
        String clientId = (String) data.get("client");
        Assert.hasText(clientId, "clientId must have value");
        String type = (String) data.get("type");
        Map<String,String> map;

        password = (String) authentication.getCredentials();
        //如果你是调用user服务，这边不用注掉
        //map = userClient.checkUsernameAndPassword(getUserServicePostObject(username, password, type));
        //map = checkUsernameAndPassword(getUserServicePostObject(username, password, type));
        map = checkUsernameAndPassword(username, password, type);


        String userId = map.get("userId");
        if (StringUtils.isBlank(userId)) {
            String errorCode = map.get("code");
            throw new BadCredentialsException(errorCode);
        }
        CustomUserDetails customUserDetails = buildCustomUserDetails(username, password, userId, clientId);
        return new CustomAuthenticationToken(customUserDetails);
    }

    private CustomUserDetails buildCustomUserDetails(String username, String password, String userId, String clientId) {
        return new CustomUserDetails.CustomUserDetailsBuilder()
                .withUserId(userId)
                .withPassword(password)
                .withUsername(username)
                .withClientId(clientId)
                .build();
    }

    /*private Map<String, String> getUserServicePostObject(String username, String password, String type) {
        Map<String, String> requestParam = new HashMap<String, String>();
        requestParam.put("userName", username);
        requestParam.put("password", password);
        if (StringUtils.isNotBlank(type)) {
            requestParam.put("type", type);
        }
        return requestParam;
    }*/

    //模拟调用user服务的方法
    /*private Map checkUsernameAndPassword(Map map) {

        //checkUsernameAndPassword
        Map ret = new HashMap();
        ret.put("userId", UUID.randomUUID().toString());

        return ret;
    }*/

    private Map<String,String> checkUsernameAndPassword(String username, String password, String type) {
        Map<String,String> result = Maps.newHashMap();

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name",username);
        List<SysUser> userList = sysUserMapper.selectList(wrapper);
        if(userList == null || userList.isEmpty()) {
            ErrorCode errorCode = new ErrorCode(100,"用户" + username + "不存在!","用户" + username + "不存在!");
            throw new CustomAuthenticationException(HttpStatus.BAD_REQUEST,errorCode);
        }

        if(userList.size() > 1) {
            ErrorCode errorCode = new ErrorCode(110,"用户" + username + "不唯一!","用户" + username + "不唯一!");
            throw new CustomAuthenticationException(HttpStatus.BAD_REQUEST,errorCode);
        }

        SysUser user = userList.get(0);

        if(!passwordEncoder.matches(password,user.getPwd())){
            ErrorCode errorCode = new ErrorCode(120,"密码输入错误！","密码输入错误！");
            throw new CustomAuthenticationException(HttpStatus.BAD_REQUEST,errorCode);
        }

        result.put("userId", String.valueOf(user.getId()));

        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
