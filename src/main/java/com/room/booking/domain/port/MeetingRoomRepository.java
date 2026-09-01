package com.room.booking.domain.port;

import com.room.booking.domain.model.MeetingRoom;

import java.util.List;

public interface MeetingRoomRepository {

    List<MeetingRoom> findAll();

    /**
     * Locks rooms that can host {@code people} people, smallest capacity first.
     */
    List<MeetingRoom> lockByMinimumCapacity(int people);
}
