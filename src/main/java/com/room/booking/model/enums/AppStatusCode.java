package com.room.booking.model.enums;

import com.room.booking.constants.AppConstants;
import lombok.Getter;

@Getter
public enum AppStatusCode {

    SYSTEM_ERROR(AppConstants.SYSTEM_ERROR_MESSAGE),
    INVALID_REQUEST(AppConstants.INVALID_REQUEST_MESSAGE),
    MAINTENANCE_WINDOW(AppConstants.MAINTENANCE_WINDOW_MESSAGE),
    NO_ROOM_AVAILABLE(AppConstants.NO_ROOM_AVAILABLE_MESSAGE),
    CAPACITY_EXCEEDED(AppConstants.CAPACITY_EXCEEDED_MESSAGE),
    SUCCESS(AppConstants.SUCCESS_MESSAGE);


    private final String message;

    AppStatusCode(String message) {
        this.message = message;
    }

}
