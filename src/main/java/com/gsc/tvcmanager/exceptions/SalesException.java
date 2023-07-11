package com.gsc.tvcmanager.exceptions;

public class SalesException extends RuntimeException {

    public SalesException(String s) {
        super(s);
    }

    public SalesException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
