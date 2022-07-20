package com.pickpick.controller;

import com.pickpick.exception.SubscriptionDuplicatedException;
import com.pickpick.exception.SubscriptionNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            SubscriptionDuplicatedException.class,
            SubscriptionNotExistException.class
    })
    public void handleBadRequest() {
    }
}
