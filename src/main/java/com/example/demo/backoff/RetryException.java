package com.example.demo.backoff;

public class RetryException extends RuntimeException {

    public RetryException(String message) {
        super(message);
    }

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }

}
