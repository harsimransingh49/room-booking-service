package com.room.booking.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends Exception {

    private final String status;
    private final String message;

    public AppException(String status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
