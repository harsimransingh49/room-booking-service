package com.room.booking.helper;

import com.room.booking.model.dto.BookedRoomDto;
import com.room.booking.model.entity.MeetingRoomEntity;
import com.room.booking.model.entity.RoomBookingEntity;
import com.room.booking.model.mapper.MeetingRoomMapper;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Helper class for booking specific operations
 */
@Component
public class BookingHelper {

    public boolean isRoomAvailable(MeetingRoomEntity meetingRoom, List<RoomBookingEntity> bookedRooms) {
        return bookedRooms.stream().noneMatch(bookedRoom -> meetingRoom.getId().equals(bookedRoom.getRoomId()));
    }

    public RoomBookingEntity createRoomBooking(MeetingRoomEntity selectedRoom, String requestId, String userName, Integer startMinute, Integer endMinute) {
        RoomBookingEntity roomBookingEntity = new RoomBookingEntity();
        roomBookingEntity.setBookingDate(LocalDate.now());
        roomBookingEntity.setRoomId(selectedRoom.getId());
        roomBookingEntity.setStartMinute(startMinute);
        roomBookingEntity.setEndMinute(endMinute);
        roomBookingEntity.setRequestId(requestId);
        roomBookingEntity.setUserName(userName);
        return roomBookingEntity;
    }

    public BookedRoomDto createBookedRoomDto(Pair<MeetingRoomEntity, RoomBookingEntity> bookingDetails, String requestId) {
        BookedRoomDto bookedRoomDto = MeetingRoomMapper.INSTANCE.mapMeetingRoomEntityToBookedRoomDto(bookingDetails.a);
        bookedRoomDto.setRequestId(requestId);
        bookedRoomDto.setBookingId(bookingDetails.b.getId());
        return bookedRoomDto;
    }
}
