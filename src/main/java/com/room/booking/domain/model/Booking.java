package com.room.booking.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record Booking(
        UUID id,
        MeetingRoom room,
        String userName,
        int people,
        LocalDate date,
        TimeRange time,
        String idempotencyKey,
        Instant createdAt
) {

    public static final int MIN_PEOPLE = 2;

    public Booking {
        if (id == null) {
            throw new IllegalArgumentException("Booking id is required");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room is required");
        }
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("userName is required");
        }
        if (people < MIN_PEOPLE) {
            throw new IllegalArgumentException("At least " + MIN_PEOPLE + " people are required");
        }
        if (!room.canHost(people)) {
            throw new IllegalArgumentException(room.name() + " cannot host " + people + " people");
        }
        if (date == null) {
            throw new IllegalArgumentException("Booking date is required");
        }
        if (time == null) {
            throw new IllegalArgumentException("Booking time is required");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("createdAt is required");
        }
        userName = userName.trim();
        idempotencyKey = blankToNull(idempotencyKey);
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
