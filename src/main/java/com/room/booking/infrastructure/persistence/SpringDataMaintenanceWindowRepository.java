package com.room.booking.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface SpringDataMaintenanceWindowRepository extends JpaRepository<MaintenanceWindowEntity, Long> {

    @Query("""
            select count(w) from MaintenanceWindowEntity w
            where w.startMinute < :endMinute and w.endMinute > :startMinute
            """)
    long countOverlapping(@Param("startMinute") int startMinute, @Param("endMinute") int endMinute);
}
