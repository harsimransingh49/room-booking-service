package com.room.booking.service;

public interface MaintenanceService {

    boolean checkForMaintenanceWindow(Integer startMinute, Integer endMinute);
}
