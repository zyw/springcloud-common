package cn.v5cn.security2.security.handler;

import cn.v5cn.security2.security.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *
 * @FileName: MyAccessDeniedHandler
 * @Company:
 * @author    ljy
 * @Date      2018年05月15日
 * @version   1.0.0
 * @remark:   自定义权限不足 需要做的业务操作
 * @explain   当用户登录系统后访问资源时因权限不足则会进入到此类并执行相关业务
 *
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        StringBuilder msg = new StringBuilder("请求：");
        msg.append(request.getRequestURI()).append(" 权限不足，无法访问系统资源.");
        LOGGER.info(msg.toString());
        ResultUtil.result(response, HttpStatus.PAYMENT_REQUIRED,"1402",msg.toString());
    }
}
