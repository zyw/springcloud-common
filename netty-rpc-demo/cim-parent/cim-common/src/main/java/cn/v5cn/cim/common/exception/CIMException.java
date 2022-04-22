package cn.v5cn.cim.common.exception;

import cn.v5cn.cim.common.enums.StatusEnum;

/**
 * @author crossoverJie
 */
public class CIMException extends GenericException {

    public CIMException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CIMException(Exception e, String errorCode, String errorMessage) {
        super(e, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CIMException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public CIMException(StatusEnum statusEnum) {
        super(statusEnum.getMessage());
        this.errorMessage = statusEnum.message();
        this.errorCode = statusEnum.getCode();
    }

    public CIMException(StatusEnum statusEnum, String message) {
        super(message);
        this.errorMessage = message;
        this.errorCode = statusEnum.getCode();
    }

    public CIMException(Exception oriEx) {
        super(oriEx);
    }

    public CIMException(Throwable oriEx) {
        super(oriEx);
    }

    public CIMException(String message, Exception oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }

    public CIMException(String message, Throwable oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }


    public static boolean isResetByPeer(String msg) {
        if ("Connection reset by peer".equals(msg)) {
            return true;
        }
        return false;
    }
}
