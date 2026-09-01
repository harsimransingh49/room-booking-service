package com.room.booking.api.dto;

import com.room.booking.domain.model.Booking;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        Long roomId,
        String roomName,
        int roomCapacity,
        String userName,
        int people,
        LocalDate date,
        String startTime,
        String endTime,
        Instant createdAt
) {

    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.id(),
                booking.room().id(),
                booking.room().name(),
                booking.room().capacity(),
                booking.userName(),
                booking.people(),
                booking.date(),
                booking.time().startClock(),
                booking.time().endClock(),
                booking.createdAt()
        );
    }
}
