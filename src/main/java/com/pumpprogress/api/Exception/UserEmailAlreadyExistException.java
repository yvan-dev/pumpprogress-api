package com.pumpprogress.api.Exception;

public class UserEmailAlreadyExistException extends RuntimeException {
    public UserEmailAlreadyExistException(String message) {
        super(message);
    }
}
