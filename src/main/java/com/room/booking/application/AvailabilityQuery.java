package com.room.booking.application;

import java.time.LocalDate;

public record AvailabilityQuery(
        String startTime,
        String endTime,
        LocalDate date,
        Integer people
) {
}
