package com.room.booking.domain.port;

import com.room.booking.domain.model.TimeRange;

public interface MaintenanceWindowRepository {

    boolean overlaps(TimeRange time);
}
