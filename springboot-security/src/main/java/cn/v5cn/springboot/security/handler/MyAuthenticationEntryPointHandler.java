package cn.v5cn.springboot.security.handler;

import cn.v5cn.springboot.security.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *
 * @FileName: MyAuthenticationEntryPointHandler
 * @Company:
 * @author    ljy
 * @Date      2018年05月15日
 * @version   1.0.0
 * @remark:   认证失败 需要做的业务操作
 * @explain   当检测到用户访问系统资源认证失败时则会进入到此类并执行相关业务
 *
 */
@Component
public class MyAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyAuthenticationEntryPointHandler.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        StringBuilder msg = new StringBuilder("请求访问: ");
        msg.append(request.getRequestURI()).append(" 接口， 因为登录超时，无法访问系统资源.");
        LOGGER.info(msg.toString());
        ResponseUtil.out(response, ResponseUtil.resultMap(402,msg.toString()));
    }
}
