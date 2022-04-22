package cn.v5cn.springboot.security.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    private final static Logger log = LoggerFactory.getLogger(ResponseUtil.class);
    /**
     *  使用response输出JSON
     * @param response
     * @param resultMap
     */
    public static void out(ServletResponse response, Map<String, Object> resultMap){

        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(JSONObject.toJSONString(resultMap));
        } catch (Exception e) {
            log.error(e + "输出JSON出错");
        }finally{
            if(out!=null){
                out.flush();
                out.close();
            }
        }
    }

    public static Map<String, Object> resultMap(Integer code, String msg){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("message", msg);
        resultMap.put("status", code);
        return resultMap;
    }

    public static Map<String, Object> resultMap( Integer status, String msg, Object data){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("message", msg);
        resultMap.put("status", status);
        resultMap.put("data", data);
        return resultMap;
    }
}
