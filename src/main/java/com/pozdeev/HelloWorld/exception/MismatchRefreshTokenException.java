package com.pozdeev.HelloWorld.exception;

public class MismatchRefreshTokenException extends RuntimeException {

    public MismatchRefreshTokenException(String message) {
        super(message);
    }
}
