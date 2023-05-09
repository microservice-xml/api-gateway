package com.gateway.apigateway.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BaseException extends RuntimeException{

    protected HttpStatus status;

    protected int statusCode;

    public BaseException(String message, HttpStatus status, int statusCode) {
        super(message);
        this.status = status;
        this.statusCode = statusCode;
    }
}
