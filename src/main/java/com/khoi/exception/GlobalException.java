package com.khoi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetail> sellerExceptionHandler(SellerException se, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setError(se.getMessage());
        errorDetail.setDetail(request.getDescription(false));
        errorDetail.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetail> sellerExceptionHandler(ProductException se, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setError(se.getMessage());
        errorDetail.setDetail(request.getDescription(false));
        errorDetail.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
