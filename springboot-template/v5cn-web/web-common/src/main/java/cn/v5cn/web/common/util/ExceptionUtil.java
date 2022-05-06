package cn.v5cn.web.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author zyw
 */
@Slf4j
public class ExceptionUtil {
    /**
     * 打印异常信息
     * @param e 异常类
     * @return 返回异常消息
     */
    public static String getMessage(Exception e) {
        String swStr = null;
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            swStr = sw.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
        return swStr;
    }
}
