package com.cnolem.test.springboot.app.exception;

public class DineroInsuficienteException extends RuntimeException {
    public DineroInsuficienteException(String message) {
        super(message);
    }
}
