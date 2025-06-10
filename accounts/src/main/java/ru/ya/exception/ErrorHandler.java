/*
package ru.ya.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.nio.channels.ClosedChannelException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public String handleServiceIsUnavailable(final HttpServerErrorException.InternalServerError e) {
        return "service-is-unavailable.html";
    }

    @ExceptionHandler
    public String handleServiceIsUnavailable2(final ResourceAccessException e) {
        return "service-is-unavailable.html";
    }

    @ExceptionHandler
    public String handleServiceIsUnavailable3(final ClosedChannelException e) {
        return "service-is-unavailable.html";
    }
}*/
