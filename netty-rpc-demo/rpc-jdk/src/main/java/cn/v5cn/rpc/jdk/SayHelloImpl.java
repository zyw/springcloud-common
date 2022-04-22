package cn.v5cn.rpc.jdk;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-27 16:09
 */
public class SayHelloImpl implements SayHello {
    @Override
    public String say(String name) {
        return name + " Hello!";
    }
}
