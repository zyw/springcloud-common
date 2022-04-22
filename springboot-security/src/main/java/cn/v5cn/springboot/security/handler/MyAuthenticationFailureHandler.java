package cn.v5cn.springboot.security.handler;

import cn.v5cn.springboot.security.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *
 * @FileName: MyAuthenticationFailureHandler
 * @Company:
 * @author    ljy
 * @Date      2018年05月15日
 * @version   1.0.0
 * @remark:   用户登录系统失败后 需要做的业务操作
 * @explain   当用户登录系统失败后则会进入到此类并执行相关业务
 *
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            //可在此记录登录失败次数，进行锁定
            ResponseUtil.out(response, ResponseUtil.resultMap(401,"用户名或密码错误"));
        } else if (e instanceof DisabledException) {
            ResponseUtil.out(response, ResponseUtil.resultMap(401,"账户被禁用，请联系管理员"));
            //可以新增登录异常次数超限LoginFailLimitException
        } else {
            ResponseUtil.out(response, ResponseUtil.resultMap(401,"登录失败"));
        }
    }
}
