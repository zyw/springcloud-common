package cn.v5cn.cim.route.exception;

import cn.v5cn.cim.common.exception.CIMException;
import cn.v5cn.cim.common.res.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author crossoverJie
 */
@ControllerAdvice
public class ExceptionHandlingController {

    private static Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ExceptionHandler(CIMException.class)
    @ResponseBody
    public BaseResponse handleAllExceptions(CIMException ex) {
        logger.error("exception", ex);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ex.getErrorCode());
        baseResponse.setMessage(ex.getMessage());
        return baseResponse;
    }
}
