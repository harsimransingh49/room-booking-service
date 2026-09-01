package com.room.booking.api.dto;

import com.room.booking.domain.model.MeetingRoom;

public record RoomResponse(Long id, String name, int capacity) {

    public static RoomResponse from(MeetingRoom room) {
        return new RoomResponse(room.id(), room.name(), room.capacity());
    }
}
