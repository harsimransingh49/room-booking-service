package com.room.booking.domain.model;

public class CapacityExceededException extends RuntimeException {

    public CapacityExceededException(int people) {
        super("No room can host " + people + " people");
    }
}
