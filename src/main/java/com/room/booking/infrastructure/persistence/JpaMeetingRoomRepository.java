package com.room.booking.infrastructure.persistence;

import com.room.booking.domain.model.MeetingRoom;
import com.room.booking.domain.port.MeetingRoomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaMeetingRoomRepository implements MeetingRoomRepository {

    private final SpringDataMeetingRoomRepository rooms;

    public JpaMeetingRoomRepository(SpringDataMeetingRoomRepository rooms) {
        this.rooms = rooms;
    }

    @Override
    public List<MeetingRoom> findAll() {
        return rooms.findAllByOrderByCapacityAscIdAsc().stream()
                .map(BookingMapper::toRoom)
                .toList();
    }

    @Override
    public List<MeetingRoom> lockByMinimumCapacity(int people) {
        return rooms.lockByMinimumCapacity(people).stream()
                .map(BookingMapper::toRoom)
                .toList();
    }
}
