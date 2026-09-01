package com.room.booking.domain.model;

public record MeetingRoom(Long id, String name, int capacity) {

    public MeetingRoom {
        if (id == null) {
            throw new IllegalArgumentException("Room id is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Room name is required");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Room capacity must be positive");
        }
    }

    public boolean canHost(int people) {
        return capacity >= people;
    }
}
