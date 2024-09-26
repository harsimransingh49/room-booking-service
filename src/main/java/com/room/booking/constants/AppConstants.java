package com.room.booking.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {

    public static final long MIN_PEOPLE = 2;
    public static final String MIN_PEOPLE_MESSAGE = "No. of people cannot be less than 2";
    public static final String TIME_REGEX = "([01][0-9]|2[0-3]):[0-5][0-9]";
    public static final String INVALID_TIME_FORMAT_MESSAGE = "Please enter time in HH:mm format";
    public static final String INVALID_TIME_DURATION_MESSAGE = "Please enter a valid time duration in intervals of 15 minutes";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String SYSTEM_ERROR_MESSAGE = "Something went wrong.";
    public static final String INVALID_REQUEST_MESSAGE = "Invalid request.";
    public static final String MAINTENANCE_WINDOW_MESSAGE = "Sorry, rooms are unavailable due to maintenance purposes.";
    public static final String NO_ROOM_AVAILABLE_MESSAGE = "Sorry, there is room available for the given time.";
    public static final String CAPACITY_EXCEEDED_MESSAGE = "Sorry, there is no room available for the given capacity";
    public static final String SUCCESS_MESSAGE = "Success";

}
