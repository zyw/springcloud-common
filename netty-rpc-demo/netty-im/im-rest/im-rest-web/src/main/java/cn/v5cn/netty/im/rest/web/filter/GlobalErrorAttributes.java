package cn.v5cn.netty.im.rest.web.filter;

import cn.v5cn.netty.im.common.exception.ImException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private Logger logger = LoggerFactory.getLogger(GlobalErrorWebExceptionHandler.class);

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {

        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        errorAttributes.put("path", request.path());
        errorAttributes.put("status", 500);
        final Throwable error = this.getError(request);

        logger.error("[rest] unknown error", this.getError(request));

        if(error instanceof ResponseStatusException) {
            return super.getErrorAttributes(request, includeStackTrace);
        }
        if(error instanceof ImException) {
            ImException e = (ImException) error;
            errorAttributes.put("msg", e.getMessage());
        } else if(error instanceof IllegalArgumentException) {
            IllegalArgumentException e = (IllegalArgumentException) error;
            errorAttributes.put("msg", e.getMessage());
        } else {
            errorAttributes.put("msg", "服务器繁忙，请稍后再试！");
        }
        return errorAttributes;
    }
}
