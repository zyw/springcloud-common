package cn.v5cn.cim.common.enums;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author crossoverJie
 */

public enum SystemCommandEnum {

    ;

    /**
     * 枚举值码
     */
    private final String commandType;
    /**
     * 枚举描述
     */
    private final String desc;
    /**
     * 实现类
     */
    private final String clazz;

    /**
     * 构建一个 。
     * @param commandType 枚举值码
     * @param desc 枚举描述
     * @param clazz 实现类
     */
    SystemCommandEnum(String commandType, String desc, String clazz) {
        this.commandType = commandType;
        this.desc = desc;
        this.clazz = clazz;
    }

    /**
     * 得到枚举值码。
     * @return 枚举值码。
     */
    public String getCommandType() {
        return commandType;
    }

    /**
     * 得到枚举描述。
     * @return 枚举描述。
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 获取 class。
     * @return class。
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * 得到枚举值码。
     * @return 枚举值码。
     */
    public String code() {
        return commandType;
    }

    /**
     * 得到枚举描述。
     * @return 枚举描述。
     */
    public String message() {
        return desc;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static Map<String,String> getAllStatusCode() {
        Map<String,String> result = new HashMap<>();
        Stream.of(values()).forEach(item -> {
            result.put(item.getCommandType(), item.getDesc());
        });
        return result;
    }

    public static Map<String,String> getAllClazz() {
        Map<String,String> result = new HashMap<>();
        Stream.of(values()).forEach(item -> {
            result.put(item.getCommandType().trim(), "cn.v5cn.cim.client.service.impl.command." + item.getClazz());
        });
        return result;
    }

}
