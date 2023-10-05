package com.example.fintechspring.exceptions;

public class BadArgumentsException extends Exception{
    public BadArgumentsException() {
        super();
    }

    public BadArgumentsException(String message) {
        super(message);
    }
}
