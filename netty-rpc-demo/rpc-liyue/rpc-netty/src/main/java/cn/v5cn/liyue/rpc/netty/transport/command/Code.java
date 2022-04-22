package cn.v5cn.liyue.rpc.netty.transport.command;

import java.util.HashMap;
import java.util.Map;

/**
 * 相应Code
 * @author LiYue
 * Date: 2019/9/23
 */
public enum Code {
    /**
     * 成功
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * 没有提供
     */
    NO_PROVIDER(-2, "NO_PROVIDER"),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(-1, "UNKNOWN_ERROR");
    private static Map<Integer,Code> codes = new HashMap<>();
    private int code;
    private String message;

    static {
        for (Code value : Code.values()) {
            codes.put(value.code,value);
        }
    }

    Code(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public static Code valueOf(int code) {
        return codes.get(code);
    }

    public int getCode() {
        return code;
    }

    public String getMessage(Object... args) {
        if(args.length < 1) {
            return message;
        }
        return String.format(message,args);
    }
}
