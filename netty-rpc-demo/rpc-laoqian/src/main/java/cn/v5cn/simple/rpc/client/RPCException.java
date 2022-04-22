package cn.v5cn.simple.rpc.client;

/**
 * 定义客户端异常，用于统一抛出RPC错误
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 22:30
 */
public class RPCException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RPCException() {
    }

    public RPCException(String message) {
        super(message);
    }

    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }

    public RPCException(Throwable cause) {
        super(cause);
    }
}
