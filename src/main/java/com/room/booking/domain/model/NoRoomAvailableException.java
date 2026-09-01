package com.room.booking.domain.model;

import java.time.LocalDate;

public class NoRoomAvailableException extends RuntimeException {

    public NoRoomAvailableException(LocalDate date, TimeRange time) {
        super("No room is available on " + date + " from " + time.startClock() + " to " + time.endClock());
    }
}
