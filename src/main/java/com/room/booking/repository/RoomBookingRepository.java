package com.room.booking.repository;

import com.room.booking.model.entity.RoomBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBookingEntity, Long> {

    List<RoomBookingEntity> findByBookingDateAndStartMinuteLessThanAndEndMinuteGreaterThan(LocalDate bookingDate, Integer endMinute, Integer startMinute);

}
