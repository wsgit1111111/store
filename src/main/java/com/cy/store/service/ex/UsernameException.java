package com.cy.store.service.ex;

/**
 * 用户名被占用异常
 */
public class UsernameException extends ServiceException{
    public UsernameException() {
        super();
    }

    public UsernameException(String message) {
        super(message);
    }

    public UsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameException(Throwable cause) {
        super(cause);
    }

    protected UsernameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
