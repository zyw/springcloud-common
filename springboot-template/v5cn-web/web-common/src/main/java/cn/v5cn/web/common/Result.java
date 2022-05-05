package cn.v5cn.web.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zyw
 */
@Data
public class Result implements Serializable {

    private Integer code;

    private String message;

    private Object data;

    /**
     * 构造器私有
     */
    private Result(){}

    /**
     * 通用返回成功
     * @return
     */
    public static Result ok() {
        Result r = new Result();
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    /**
     * 通用返回失败，未知错误
     * @return
     */
    public static Result error() {
        Result r = new Result();
        r.setCode(ResultCodeEnum.UNKNOWN_ERROR.getCode());
        r.setMessage(ResultCodeEnum.UNKNOWN_ERROR.getMessage());
        return r;
    }

    /**
     * 指定错误类型
     * @param result
     * @return
     */
    public static Result failure(ResultCodeEnum result) {
        Result r = new Result();
        r.setCode(result.getCode());
        r.setMessage(result.getMessage());
        return r;
    }

    /**
     * 设置结果，形参为结果枚举
     * @param result
     * @return
     */
    public static Result setResult(ResultCodeEnum result) {
        Result r = new Result();
        r.setCode(result.getCode());
        r.setMessage(result.getMessage());
        return r;
    }

    /**------------使用链式编程，返回类本身-----------**/

    /**
     * 自定义返回数据
     * @param data
     * @return
     */
    public Result data(Object data) {
        this.setData(data);
        return this;
    }

    /**
     * 通用设置data
     * @param key
     * @param value
     * @return
     */
    public Result data(String key,Object value) {
        Map<String, Object> tempData = new HashMap<>();
        tempData.put(key, value);
        this.setData(tempData);
        return this;
    }

    /**
     * 自定义状态信息
     * @param message
     * @return
     */
    public Result message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * 自定义状态码
     * @param code
     * @return
     */
    public Result code(Integer code) {
        this.setCode(code);
        return this;
    }
}
