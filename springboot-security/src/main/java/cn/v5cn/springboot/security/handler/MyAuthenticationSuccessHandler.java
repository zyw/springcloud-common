package cn.v5cn.springboot.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/***
 *
 * @FileName: MyAuthenticationSuccessHandler
 * @Company:
 * @author    ljy
 * @Date      2018年05月15日
 * @version   1.0.0
 * @remark:   用户登录系统成功后 需要做的业务操作
 * @explain   当用户登录系统成功后则会进入到此类并执行相关业务
 *
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

    }
}
