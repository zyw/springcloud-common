package cn.v5cn.springcloud.authserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CustomAuthenticationException extends AuthenticationException {

    private final HttpStatus status;

    private final int code;

    private final String message;

    private final String detailMessage;

    public CustomAuthenticationException(HttpStatus status, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = status;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.detailMessage = errorCode.getDetailMessage();
    }

    public CustomAuthenticationException(HttpStatus status) {
        super(null);
        this.status = status;
        code = 0;
        message = null;
        detailMessage = null;
    }

    public CustomAuthenticationException(HttpStatus status, ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(),cause);
        this.status = status;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        detailMessage = (cause.getMessage() != null ? cause.getMessage() : "") + toStackTrace(cause);
    }

    private String toStackTrace(Throwable e) {
        StringWriter errorStackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(errorStackTrace));
        return errorStackTrace.toString();

    }

    public HttpStatus getStatus() {
        return status;
    }

    public ErrorCode getCode() {
        return new ErrorCode(code,message,detailMessage);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
