package cn.v5cn.springboot.security.jwt;

import cn.v5cn.springboot.security.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *
 * @FileName: JwtAuthenticationEntryPoint
 * @Company:
 * @author    ljy
 * @Date      2018年05月120日
 * @version   1.0.0
 * @remark:   jwt 认证处理类
 *
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        StringBuffer msg = new StringBuffer("请求访问: ");
        msg.append(httpServletRequest.getRequestURI()).append(" 接口， 经jwt 认证失败，无法访问系统资源.");
        LOGGER.info(msg.toString());
        LOGGER.info(e.toString());
        // 用户登录时身份认证未通过
        if(e instanceof BadCredentialsException) {
            LOGGER.info("用户登录时身份认证失败.");
            ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(402,msg.toString()));
        }else if(e instanceof InsufficientAuthenticationException){
            LOGGER.info("缺少请求头参数,Authorization传递是token值所以参数是必须的.");
            ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(402,msg.toString()));
        }else {
            LOGGER.info("用户token无效.");
            ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(402,msg.toString()));
        }
    }
}
