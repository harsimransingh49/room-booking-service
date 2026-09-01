package com.room.booking.application;

import java.time.LocalDate;

public record BookRoomCommand(
        String userName,
        int people,
        String startTime,
        String endTime,
        LocalDate date,
        String idempotencyKey
) {
}
