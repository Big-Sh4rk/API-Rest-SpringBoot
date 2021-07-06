package com.hackerrank.eshopping.product.dashboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequest extends RuntimeException{

    public BadRequest(String detail){
        super(detail);
    }
}
