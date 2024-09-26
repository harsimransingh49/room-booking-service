package com.room.booking.repository;

import com.room.booking.model.entity.MaintenanceTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceTimeEntity, Long> {

    long countByStartMinuteLessThanAndEndMinuteGreaterThan(Integer endMinute, Integer startMinute);
}
