package com.room.booking.service.impl;

import com.room.booking.repository.MaintenanceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceImplTest {

    @InjectMocks
    MaintenanceServiceImpl maintenanceService;

    @Mock
    MaintenanceRepository maintenanceRepository;

    @Test
    void checkForMaintenanceWindowTrueTest() {
        Mockito.when(maintenanceRepository.countByStartMinuteLessThanAndEndMinuteGreaterThan(Mockito.anyInt(), Mockito.anyInt())).thenReturn(1L);
        Assertions.assertTrue(maintenanceService.checkForMaintenanceWindow(0, 15));
    }

    @Test
    void checkForMaintenanceWindowFalseTest() {
        Mockito.when(maintenanceRepository.countByStartMinuteLessThanAndEndMinuteGreaterThan(Mockito.anyInt(), Mockito.anyInt())).thenReturn(0L);
        Assertions.assertFalse(maintenanceService.checkForMaintenanceWindow(0, 15));
    }
}