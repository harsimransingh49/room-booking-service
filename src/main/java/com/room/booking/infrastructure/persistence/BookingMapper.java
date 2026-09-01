package com.room.booking.infrastructure.persistence;

import com.room.booking.domain.model.Booking;
import com.room.booking.domain.model.MeetingRoom;
import com.room.booking.domain.model.TimeRange;

final class BookingMapper {

    private BookingMapper() {
    }

    static MeetingRoom toRoom(MeetingRoomEntity entity) {
        return new MeetingRoom(entity.getId(), entity.getName(), entity.getCapacity());
    }

    static Booking toBooking(BookingEntity entity) {
        return new Booking(
                entity.getId(),
                toRoom(entity.getRoom()),
                entity.getUserName(),
                entity.getPeople(),
                entity.getBookingDate(),
                new TimeRange(entity.getStartMinute(), entity.getEndMinute()),
                entity.getIdempotencyKey(),
                entity.getCreatedAt()
        );
    }
}
