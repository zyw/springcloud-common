package cn.v5cn.dynamic.proxy.bytebuddy;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-03-02 23:04
 */
public class Target {
    public static String hello(String name) {
        return "Hello " + name + "!";
    }
}
