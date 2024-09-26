package com.room.booking.processor;

import com.room.booking.model.entity.MeetingRoomEntity;
import com.room.booking.model.entity.RoomBookingEntity;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

public interface RoomBookingProcessor {
    Pair<MeetingRoomEntity, RoomBookingEntity> bookMeetingRoom(List<MeetingRoomEntity> meetingRoomEntities, String requestId, String userName, Integer startMinute, Integer endMinute);
}
