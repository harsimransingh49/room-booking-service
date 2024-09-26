package com.room.booking.processor;

import com.room.booking.helper.BookingHelper;
import com.room.booking.model.entity.BookingSlotEntity;
import com.room.booking.model.entity.MeetingRoomEntity;
import com.room.booking.model.entity.RoomBookingEntity;
import com.room.booking.repository.BookingSlotRepository;
import com.room.booking.repository.RoomBookingRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RoomBookingProcessorImplTest {

    @InjectMocks
    RoomBookingProcessorImpl roomBookingProcessorImpl;

    @Mock
    BookingSlotRepository bookingSlotRepository;

    @Mock
    RoomBookingRepository roomBookingRepository;

    @Spy
    BookingHelper bookingHelper;

    @Test
    void bookMeetingRoomNoAvailableRoomTest() {
        MeetingRoomEntity meetingRoomEntity = new MeetingRoomEntity();
        meetingRoomEntity.setId(1L);
        BookingSlotEntity bookingSlotEntity = new BookingSlotEntity();
        bookingSlotEntity.setStartMinute(0);
        bookingSlotEntity.setEndMinute(15);
        RoomBookingEntity roomBookingEntity = new RoomBookingEntity();
        roomBookingEntity.setRoomId(1L);
        Mockito.when(bookingSlotRepository.findByStartMinuteLessThanAndEndMinuteGreaterThan(Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(bookingSlotEntity));
        Mockito.when(roomBookingRepository.findByBookingDateAndStartMinuteLessThanAndEndMinuteGreaterThan(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(roomBookingEntity));
        Pair<MeetingRoomEntity, RoomBookingEntity> bookingDetails = roomBookingProcessorImpl.bookMeetingRoom(List.of(meetingRoomEntity), "test", "test", 0, 15);
        Assertions.assertNull(bookingDetails);

    }

    @Test
    void bookMeetingRoomSuccessTest() {
        MeetingRoomEntity meetingRoomEntity = new MeetingRoomEntity();
        meetingRoomEntity.setId(1L);
        BookingSlotEntity bookingSlotEntity = new BookingSlotEntity();
        bookingSlotEntity.setStartMinute(0);
        bookingSlotEntity.setEndMinute(15);
        Mockito.when(bookingSlotRepository.findByStartMinuteLessThanAndEndMinuteGreaterThan(Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(bookingSlotEntity));
        Mockito.when(roomBookingRepository.findByBookingDateAndStartMinuteLessThanAndEndMinuteGreaterThan(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());
        Pair<MeetingRoomEntity, RoomBookingEntity> bookingDetails = roomBookingProcessorImpl.bookMeetingRoom(List.of(meetingRoomEntity), "test", "test", 0, 15);
        Assertions.assertNotNull(bookingDetails);
        Assertions.assertEquals(1L, bookingDetails.b.getRoomId());
        Assertions.assertEquals(1L, bookingDetails.a.getId());
    }
}