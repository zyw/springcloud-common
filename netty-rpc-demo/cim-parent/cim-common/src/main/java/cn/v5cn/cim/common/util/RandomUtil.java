package cn.v5cn.cim.common.util;

/**
 * @author crossoverJie
 */
public class RandomUtil {

    public static int getRandom() {
        double random = Math.random();
        return (int)(random * 10);
    }
}
