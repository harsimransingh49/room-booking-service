package com.room.booking.exception.handler;

import com.room.booking.model.dto.ResponseDto;
import com.room.booking.model.enums.AppStatusCode;
import com.room.booking.exception.AppException;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

/**
 * The app wide exception handler class
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleCustomException(Exception exception) {
        if (exception instanceof AppException ex) {
            String message = ex.getMessage();
            String status = ex.getStatus();
            return this.buildErrorResponse(message, status);
        } else {
            return this.buildErrorResponse(AppStatusCode.SYSTEM_ERROR.getMessage(), AppStatusCode.SYSTEM_ERROR.name());
        }
    }

    private ResponseEntity<Object> buildErrorResponse(String message, String status) {
        ResponseDto<Object> errorResponse = ResponseDto.builder().statusCode(status).message(message).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + " : " + e.getDefaultMessage()).collect(Collectors.joining(", "));
        if (StringUtils.isBlank(message) && !CollectionUtils.isEmpty(ex.getBindingResult().getGlobalErrors())) {
            message = ex.getBindingResult().getGlobalErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        }
        return this.buildErrorResponse(message, AppStatusCode.INVALID_REQUEST.name());
    }

}
