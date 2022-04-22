package cn.v5cn.cim.common.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author crossoverJie
 */
public enum StatusEnum {
    /**
     * 成功
     */
    SUCCESS("9000", "成功"),
    /**
     * 成功
     */
    FALLBACK("8000","FALL_BACK"),
    /**
     * 参数校验失败
     */
    VALIDATION_FAIL("3000","invalid argument"),
    /**
     * 失败
     */
    FAIL("4000","Failure"),
    /**
     * 重复登录
     */
    REPEAT_LOGIN("5000", "repeat login, log out an account please!"),
    /**
     * 请求限流
     */
    REQUEST_LIMIT("6000", "请求限流"),
    /**
     * 账号不在线
     */
    OFF_LINE("7000", "你选择的账号不在线，请重新选择！"),
    /**
     *
     */
    SERVER_NOT_AVAILABLE("7100", "cim server is not available, please try again later!"),
    RECONNECT_FAIL("7200", "Reconnect fail, continue to retry!"),
    /** 登录信息不匹配 */
    ACCOUNT_NOT_MATCH("9100", "The User information you have used is incorrect!")
    ;

    /**
     * 枚举值码
     */
    private final String code;
    /**
     * 枚举描述
     */
    private final String message;

    StatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 得到枚举值码
     * @return 枚举值码
     */
    public String getCode() {
        return code;
    }

    /**
     * 得到枚举描述
     * @return 枚举描述
     */
    public String getMessage() {
        return message;
    }

    /**
     * 得到枚举值码。
     * @return 枚举值码。
     */
    public String code() {
        return code;
    }

    /**
     * 得到枚举描述。
     * @return 枚举描述。
     */
    public String message() {
        return message;
    }

    /**
     * 通过枚举值码查找枚举值。
     * @param code 查找枚举值的枚举值码。
     * @return 返回状态枚举
     * @throws IllegalArgumentException 参数异常
     */
    public static StatusEnum findStatus(String code) {
        return Stream.of(values())
                .filter(item -> item.code().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ResultInfo StatusEnum not legal:" + code));
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<StatusEnum> getAllStatus() {
        return Stream.of(values()).collect(Collectors.toList());
    }

    /**
     * 获取全部枚举值码。
     * @return 全部枚举值码。
     */
    public static List<String> getAllStatusCode() {
        return Stream.of(values()).map(StatusEnum::getCode).collect(Collectors.toList());
    }
}
