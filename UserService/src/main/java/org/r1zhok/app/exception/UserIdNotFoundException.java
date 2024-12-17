package org.r1zhok.app.exception;

public class UserIdNotFoundException extends Exception {
    public UserIdNotFoundException() {
    }

    public UserIdNotFoundException(String message) {
        super(message);
    }

    public UserIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIdNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserIdNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
