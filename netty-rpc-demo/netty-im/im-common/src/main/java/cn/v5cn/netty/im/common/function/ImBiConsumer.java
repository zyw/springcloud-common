package cn.v5cn.netty.im.common.function;

/**
 * Date: 2019-05-19
 * Time: 22:33
 *
 * @author yrw
 */
@FunctionalInterface
public interface ImBiConsumer<T, U> {
    /**
     * 对给定参数执行此操作
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @throws Exception
     */
    void accept(T t, U u) throws Exception;
}
