package com.gsc.tvcmanager.exceptions;

public class IndicatorsException extends RuntimeException {
    public IndicatorsException(String s) {
        super(s);
    }

    public IndicatorsException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
