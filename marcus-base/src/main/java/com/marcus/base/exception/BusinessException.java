package com.marcus.base.exception;

import java.io.Serializable;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/19 15:32
 **/
public class BusinessException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -6223252991613969582L;

    /** 异常级别：警告 */
    public static final int EXCEPTIONLEVEL_WARN = 500;

    /** 异常级别：错误 */
    public static final int EXCEPTIONLEVEL_ERROR = 400;

    /** 默认异常级别：警告 */
    private int code = EXCEPTIONLEVEL_WARN;

    public int getCode() {
        return code;
    }

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
