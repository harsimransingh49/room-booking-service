package com.room.booking.domain.model;

public record MaintenanceWindow(int startMinute, int endMinute) {

    public MaintenanceWindow {
        if (startMinute >= endMinute) {
            throw new IllegalArgumentException("Maintenance window must have a positive duration");
        }
    }

    public boolean overlaps(TimeRange range) {
        return startMinute < range.endMinute() && endMinute > range.startMinute();
    }
}
