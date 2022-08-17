package com.pickpick.config;

import com.pickpick.config.dto.ErrorResponse;
import com.pickpick.exception.BadRequestException;
import com.pickpick.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.error("예외 발생: ", e);
        return new ErrorResponse(e.getErrorCode(), e.getClientMessage());

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("예외 발생: ", e);
        return new ErrorResponse(e.getErrorCode(), e.getClientMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public void handleRuntimeException(final RuntimeException e) {
        log.error("예상하지 못한 에러가 발생하였습니다.", e);
    }
}
