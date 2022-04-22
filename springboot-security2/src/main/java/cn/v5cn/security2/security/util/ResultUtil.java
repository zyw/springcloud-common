package cn.v5cn.security2.security.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-05-26 23:05
 */
public class ResultUtil {

    public static void result(HttpServletResponse response, HttpStatus httpStatus, Map<String, String> resultJsonMap) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(httpStatus.value());
        response.getWriter().write(JSONObject.toJSONString(resultJsonMap));
    }
    
    public static void result(HttpServletResponse response, HttpStatus httpStatus,String code, String message) throws IOException {
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("code",code);
        resultMap.put("message",message);
        result(response,httpStatus,resultMap);
    }
}
