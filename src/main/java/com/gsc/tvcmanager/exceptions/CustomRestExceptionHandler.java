package com.gsc.tvcmanager.exceptions;

import com.gsc.tvcmanager.constants.ApiErrorConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomRestExceptionHandler {


    @ExceptionHandler(value = {IndicatorsException.class})
    public ResponseEntity<ApiError> indicatorException(IndicatorsException ex, WebRequest request) {
        ApiError apiError = new ApiError(ApiErrorConstants.ERROR_PROCESSING_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(), request.getDescription(false), ex.getCause().getMessage());

        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
