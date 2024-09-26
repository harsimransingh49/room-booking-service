package com.room.booking.service.impl;

import com.room.booking.repository.MaintenanceRepository;
import com.room.booking.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for maintenance related requests. Can be enhanced to add more maintenance slots.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    /**
     * @param startMinute Start time requested
     * @param endMinute   End time requested
     * @return true if request is in maintenance window else return false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkForMaintenanceWindow(Integer startMinute, Integer endMinute) {
        return maintenanceRepository.countByStartMinuteLessThanAndEndMinuteGreaterThan(endMinute, startMinute) > 0;
    }
}
