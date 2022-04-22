package cn.v5cn.netty.im.common.exception;

/**
 *
 * @author yrw
 */
public class ImException extends RuntimeException {

    public ImException(String message, Throwable e) {
        super(message, e);
    }

    public ImException(Throwable e) {
        super(e);
    }

    public ImException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": " + message) : s;
    }
}
