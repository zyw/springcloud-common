package cn.v5cn.web.common;

import cn.v5cn.web.common.rwv.ResponseResult;
import cn.v5cn.web.common.rwv.ResponseResultInterceptor;
import cn.v5cn.web.common.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局响应处理，包括：
 * 1. 统一异常处理
 * 2. 统一返回值包装处理
 * @author zyw
 */
@Slf4j
@ControllerAdvice
public class GlobalResultHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 判断请求 是否有包装标记
        ResponseResult responseResultAnn = (ResponseResult) request.getAttribute(ResponseResultInterceptor.RESPONSE_RESULT_ANN);
        return responseResultAnn != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest req, ServerHttpResponse resp) {
        // TODO 未处理异常情况
        if (body instanceof Result){
            return body;
        }
        log.info("返回体需要重写格式，处理中。。。。");
        return Result.ok().data(body);
    }

    /**
     * 通用异常处理方法
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
//        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));
        // TODO 添加自定义的一些异常返回值
        return Result.error();
    }
}
