package com.team3.devinit_back.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(CustomException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getErrorCode().getStatus().getReasonPhrase());
        errorDetails.put("message", ex.getErrorCode().getMessage());

        log.error("CustomException: {}", ex.getMessage());
        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Internal Server Error");
        errorDetails.put("message", ex.getMessage());

        log.error("Unhandled Exception: ", ex);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(errorDetails);
    }
}