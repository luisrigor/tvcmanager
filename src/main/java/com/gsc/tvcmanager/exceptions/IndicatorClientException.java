package com.gsc.tvcmanager.exceptions;

public class IndicatorClientException extends RuntimeException{
    public IndicatorClientException(String s) {
        super(s);
    }

    public IndicatorClientException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
