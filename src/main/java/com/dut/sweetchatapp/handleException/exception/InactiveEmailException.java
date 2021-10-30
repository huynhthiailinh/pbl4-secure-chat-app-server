package com.dut.sweetchatapp.handleException.exception;

public class InactiveEmailException extends RuntimeException {
    public InactiveEmailException(String message) {
        super(message);
    }
}
