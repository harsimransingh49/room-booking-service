package com.room.booking.domain.model;

import java.util.UUID;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(UUID id) {
        super("Booking " + id + " was not found");
    }
}
