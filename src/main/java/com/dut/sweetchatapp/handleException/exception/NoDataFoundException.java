package com.dut.sweetchatapp.handleException.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException() {
        super("No data found");
    }
}
