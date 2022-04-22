package cn.v5cn.netty.ws.pb.core.function;

/**
 * @author zyw
 */
@FunctionalInterface
public interface ImBiConsumer<T,U> {

    /**
     * 执行消息处理的函数式接口
     * @param t 类型参数1
     * @param u 类型参数2
     * @throws Exception 抛出异常
     */
    void accept(T t, U u) throws Exception;
}
