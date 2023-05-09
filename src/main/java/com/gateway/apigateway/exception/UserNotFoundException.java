package com.gateway.apigateway.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException{

    public UserNotFoundException() { super("User not found", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value());}
}
