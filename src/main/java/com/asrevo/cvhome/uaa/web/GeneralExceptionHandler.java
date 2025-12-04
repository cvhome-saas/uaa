package com.asrevo.cvhome.uaa.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        log.error("Internal Server Error", e);
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setTitle("Internal Server Error");
        return problemDetail;
    }

}
