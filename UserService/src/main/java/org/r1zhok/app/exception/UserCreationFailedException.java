package org.r1zhok.app.exception;

public class UserCreationFailedException extends Exception {

    public UserCreationFailedException() {
    }

    public UserCreationFailedException(String message) {
        super(message);
    }

    public UserCreationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCreationFailedException(Throwable cause) {
        super(cause);
    }

    public UserCreationFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
