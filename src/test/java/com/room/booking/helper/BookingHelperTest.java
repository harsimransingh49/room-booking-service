package com.room.booking.helper;

import com.room.booking.model.dto.BookedRoomDto;
import com.room.booking.model.entity.MeetingRoomEntity;
import com.room.booking.model.entity.RoomBookingEntity;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class BookingHelperTest {

    final BookingHelper bookingHelper = new BookingHelper();

    @Test
    void isRoomAvailableTestTrueCase() {
        MeetingRoomEntity meetingRoomEntity = new MeetingRoomEntity();
        meetingRoomEntity.setId(1L);
        meetingRoomEntity.setCapacity(3L);
        meetingRoomEntity.setName("Amaze");
        Assertions.assertTrue(bookingHelper.isRoomAvailable(meetingRoomEntity, new ArrayList<>()));

    }

    @Test
    void isRoomAvailableTestFalseCase() {
        RoomBookingEntity roomBookingEntity = new RoomBookingEntity();
        roomBookingEntity.setRoomId(1L);
        MeetingRoomEntity meetingRoomEntity = new MeetingRoomEntity();
        meetingRoomEntity.setId(1L);
        meetingRoomEntity.setCapacity(3L);
        meetingRoomEntity.setName("Amaze");
        Assertions.assertFalse(bookingHelper.isRoomAvailable(meetingRoomEntity, List.of(roomBookingEntity)));

    }

    @Test
    void createRoomBookingTest() {
        MeetingRoomEntity meetingRoomEntity = new MeetingRoomEntity();
        meetingRoomEntity.setId(1L);
        RoomBookingEntity roomBookingEntity = bookingHelper.createRoomBooking(meetingRoomEntity, "test", "test", 0, 15);
        Assertions.assertNotNull(roomBookingEntity);
        Assertions.assertEquals(1L, roomBookingEntity.getRoomId());
    }

    @Test
    void createBookedRoomDtoTest() {
        MeetingRoomEntity meetingRoomEntity = new MeetingRoomEntity();
        meetingRoomEntity.setId(1L);
        RoomBookingEntity roomBookingEntity = new RoomBookingEntity();
        roomBookingEntity.setId(1L);
        Pair<MeetingRoomEntity, RoomBookingEntity> bookingDetails = new Pair<>(meetingRoomEntity, roomBookingEntity);
        BookedRoomDto bookedRoomDto = bookingHelper.createBookedRoomDto(bookingDetails, "test");
        Assertions.assertNotNull(bookedRoomDto);
        Assertions.assertEquals(1L, bookedRoomDto.getId());
        Assertions.assertEquals(1L, bookedRoomDto.getBookingId());
        Assertions.assertEquals("test", bookedRoomDto.getRequestId());
    }
}