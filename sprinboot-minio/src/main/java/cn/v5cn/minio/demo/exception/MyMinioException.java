package cn.v5cn.minio.demo.exception;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-11 21:07
 */
public class MyMinioException extends RuntimeException {

    public MyMinioException() {
    }

    public MyMinioException(String message) {
        super(message);
    }

    public MyMinioException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyMinioException(Throwable cause) {
        super(cause);
    }
}
