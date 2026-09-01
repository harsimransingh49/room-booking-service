package com.room.booking.domain.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Half-open booking interval [start, end) expressed as minutes from midnight.
 * Business hours are booked in 15-minute increments.
 */
public record TimeRange(int startMinute, int endMinute) {

    public static final int SLOT_MINUTES = 15;
    public static final int MINUTES_PER_DAY = 24 * 60;
    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm");

    public TimeRange {
        if (startMinute < 0 || endMinute > MINUTES_PER_DAY || startMinute >= endMinute) {
            throw new IllegalArgumentException("End time must be after start time and within the same day");
        }
        if (startMinute % SLOT_MINUTES != 0 || endMinute % SLOT_MINUTES != 0) {
            throw new IllegalArgumentException("Times must fall on 15-minute boundaries");
        }
    }

    public boolean overlaps(TimeRange other) {
        Objects.requireNonNull(other, "other");
        return startMinute < other.endMinute && endMinute > other.startMinute;
    }

    public String startClock() {
        return toClock(startMinute);
    }

    public String endClock() {
        return toClock(endMinute);
    }

    public static TimeRange ofClock(String startTime, String endTime) {
        return new TimeRange(parseClock(startTime, "startTime"), parseClock(endTime, "endTime"));
    }

    public static int parseClock(String clock, String field) {
        if (clock == null || clock.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        try {
            LocalTime time = LocalTime.parse(clock.trim(), CLOCK);
            return time.getHour() * 60 + time.getMinute();
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(field + " must be HH:mm");
        }
    }

    private static String toClock(int minuteOfDay) {
        int bounded = Math.min(minuteOfDay, MINUTES_PER_DAY - 1);
        return LocalTime.of(bounded / 60, bounded % 60).format(CLOCK);
    }
}
