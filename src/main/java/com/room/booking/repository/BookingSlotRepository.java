package com.room.booking.repository;

import com.room.booking.model.entity.BookingSlotEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSlotRepository extends JpaRepository<BookingSlotEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<BookingSlotEntity> findByStartMinuteLessThanAndEndMinuteGreaterThan(Integer endMinute, Integer startMinute);
}
