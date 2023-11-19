package com.example.fintechspring.exceptions;

public class RuntimeSQLException extends RuntimeException{
    public RuntimeSQLException() { super(); }
    public RuntimeSQLException(String message) { super(message); }
}
