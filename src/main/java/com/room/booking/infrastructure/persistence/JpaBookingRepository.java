package com.room.booking.infrastructure.persistence;

import com.room.booking.domain.model.Booking;
import com.room.booking.domain.model.TimeRange;
import com.room.booking.domain.port.BookingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class JpaBookingRepository implements BookingRepository {

    private final SpringDataBookingRepository bookings;
    private final SpringDataMeetingRoomRepository rooms;

    public JpaBookingRepository(SpringDataBookingRepository bookings, SpringDataMeetingRoomRepository rooms) {
        this.bookings = bookings;
        this.rooms = rooms;
    }

    @Override
    public Booking save(Booking booking) {
        MeetingRoomEntity room = rooms.getReferenceById(booking.room().id());
        BookingEntity entity = new BookingEntity(
                booking.id(),
                room,
                booking.userName(),
                booking.people(),
                booking.date(),
                booking.time().startMinute(),
                booking.time().endMinute(),
                booking.idempotencyKey(),
                booking.createdAt()
        );
        return BookingMapper.toBooking(bookings.save(entity));
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return bookings.findById(id).map(BookingMapper::toBooking);
    }

    @Override
    public Optional<Booking> findByIdempotencyKey(String idempotencyKey) {
        return bookings.findByIdempotencyKey(idempotencyKey).map(BookingMapper::toBooking);
    }

    @Override
    public List<Booking> findByDate(LocalDate date) {
        return bookings.findByBookingDateOrderByStartMinuteAsc(date).stream()
                .map(BookingMapper::toBooking)
                .toList();
    }

    @Override
    public Set<Long> findOverlappingRoomIds(LocalDate date, TimeRange time) {
        return new HashSet<>(bookings.findOverlappingRoomIds(date, time.startMinute(), time.endMinute()));
    }
}
