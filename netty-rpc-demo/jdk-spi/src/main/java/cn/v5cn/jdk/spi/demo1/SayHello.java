package cn.v5cn.jdk.spi.demo1;

/**
 * <p>
 *     服务定义接口，通常在一个jar文件中定义
 * </p>
 * @author ZYW
 * @version 1.0
 * @date 2020-02-29 14:57
 */
public interface SayHello {
    /**
     * 服务定义接口定义了一个方法声明
     * @param name 名称
     * @return 返回问候语
     */
    String say(String name);
}
