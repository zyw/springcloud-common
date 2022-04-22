package cn.v5cn.netty.ws.pb.core.exception;

/**
 *
 * @author zyw
 */
public class ImLogicException extends RuntimeException {

    public ImLogicException(String message, Throwable e) {
        super(message, e);
    }

    public ImLogicException(Throwable e) {
        super(e);
    }

    public ImLogicException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": " + message) : s;
    }
}
