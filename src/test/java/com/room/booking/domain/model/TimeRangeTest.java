package com.room.booking.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeRangeTest {

    @Test
    void parsesClockAndDetectsOverlap() {
        TimeRange morning = TimeRange.ofClock("10:00", "10:30");
        assertThat(morning.startMinute()).isEqualTo(600);
        assertThat(morning.endMinute()).isEqualTo(630);
        assertThat(morning.overlaps(TimeRange.ofClock("10:15", "10:45"))).isTrue();
        assertThat(morning.overlaps(TimeRange.ofClock("10:30", "11:00"))).isFalse();
    }

    @Test
    void rejectsInvalidIntervals() {
        assertThatThrownBy(() -> TimeRange.ofClock("10:00", "10:00"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TimeRange.ofClock("10:05", "10:20"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("15-minute");
        assertThatThrownBy(() -> TimeRange.ofClock("10:00", "09:00"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
