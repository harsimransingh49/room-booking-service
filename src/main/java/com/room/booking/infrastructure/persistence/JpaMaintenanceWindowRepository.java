package com.room.booking.infrastructure.persistence;

import com.room.booking.domain.model.TimeRange;
import com.room.booking.domain.port.MaintenanceWindowRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaMaintenanceWindowRepository implements MaintenanceWindowRepository {

    private final SpringDataMaintenanceWindowRepository windows;

    public JpaMaintenanceWindowRepository(SpringDataMaintenanceWindowRepository windows) {
        this.windows = windows;
    }

    @Override
    public boolean overlaps(TimeRange time) {
        return windows.countOverlapping(time.startMinute(), time.endMinute()) > 0;
    }
}
