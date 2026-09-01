package com.room.booking.domain.model;

public class MaintenanceWindowException extends RuntimeException {

    public MaintenanceWindowException() {
        super("Rooms are unavailable during a maintenance window");
    }
}
