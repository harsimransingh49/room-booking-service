package com.room.booking.domain.port;

import com.room.booking.domain.model.Booking;
import com.room.booking.domain.model.TimeRange;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BookingRepository {

    Booking save(Booking booking);

    Optional<Booking> findById(UUID id);

    Optional<Booking> findByIdempotencyKey(String idempotencyKey);

    List<Booking> findByDate(LocalDate date);

    Set<Long> findOverlappingRoomIds(LocalDate date, TimeRange time);
}
