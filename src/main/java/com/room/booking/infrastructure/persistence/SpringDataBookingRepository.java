package com.room.booking.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringDataBookingRepository extends JpaRepository<BookingEntity, UUID> {

    Optional<BookingEntity> findByIdempotencyKey(String idempotencyKey);

    List<BookingEntity> findByBookingDateOrderByStartMinuteAsc(LocalDate bookingDate);

    @Query("""
            select b.room.id from BookingEntity b
            where b.bookingDate = :date
              and b.startMinute < :endMinute
              and b.endMinute > :startMinute
            """)
    List<Long> findOverlappingRoomIds(
            @Param("date") LocalDate date,
            @Param("startMinute") int startMinute,
            @Param("endMinute") int endMinute
    );
}
