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
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * 全局响应处理，包括：
 * 1. 统一异常处理
 * 2. 统一返回值包装处理
 * @author zyw
 */
@Slf4j
@RestControllerAdvice
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

    /****************************************************************
     * 在上面的参数验证中，验证的错误信息是通过BindingResult result参数进行接收的，
     * 在每个方法中异常处理如出一辙，特别麻烦。甚至在step 5，6都是直接将异常的堆栈信息返回给前端，
     * 这对于用来说是非常不友好的。而且有的情况下需要我们主动抛出业务异常，
     * 比方用户不能直接删除已绑定用户的角色。
    ****************************************************************/

    /**
     * 参数绑定异常类 用于表单验证时抛出的异常处理
     */
    @ExceptionHandler(BindException.class)
    public Result validatedBindException(BindException e){
        log.error(ExceptionUtil.getMessage(e));
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = e.getFieldError();
        String message = "[" + e.getAllErrors().get(0).getDefaultMessage() + "]";
        return Result.error().message(message);
    }

    /**
     * 用于方法形参中参数校验时抛出的异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handle(ValidationException e) {
        log.error(ExceptionUtil.getMessage(e));
        String errorInfo = "";
        if(e instanceof ConstraintViolationException){
            ConstraintViolationException exs = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                errorInfo = errorInfo + "[" + item.getMessage() + "]";
            }
        }
        return Result.error().message(errorInfo);
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public Result handleException(HttpRequestMethodNotSupportedException e){
        log.error(ExceptionUtil.getMessage(e));
        return Result.error().message("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 通用异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
//        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));
        // TODO 添加自定义的一些异常返回值
        return Result.error();
    }
}
