package com.room.booking.service.impl;

import com.room.booking.exception.AppException;
import com.room.booking.helper.BookingHelper;
import com.room.booking.model.dto.ResponseDto;
import com.room.booking.model.dto.RoomBookingRequestDto;
import com.room.booking.model.dto.TimeDurationRequestDto;
import com.room.booking.model.entity.MeetingRoomEntity;
import com.room.booking.model.entity.RoomBookingEntity;
import com.room.booking.model.enums.AppStatusCode;
import com.room.booking.processor.RoomBookingProcessor;
import com.room.booking.repository.MeetingRoomRepository;
import com.room.booking.repository.RoomBookingRepository;
import com.room.booking.service.MaintenanceService;
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
class BookingServiceImplTest {

    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    MaintenanceService maintenanceService;

    @Mock
    RoomBookingRepository roomBookingRepository;

    @Mock
    MeetingRoomRepository meetingRoomRepository;

    @Mock
    RoomBookingProcessor roomBookingProcessor;

    @Spy
    BookingHelper bookingHelper;

    @Test
    void checkAvailabilityForBookingMaintenanceWindowTest() {
        TimeDurationRequestDto timeDurationRequestDto = new TimeDurationRequestDto("10:00", "10:15");
        Mockito.when(maintenanceService.checkForMaintenanceWindow(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        ResponseDto<Object> responseDto = bookingService.checkAvailabilityForBooking(timeDurationRequestDto);
        Assertions.assertEquals(AppStatusCode.MAINTENANCE_WINDOW.name(), responseDto.getStatusCode());
        Assertions.assertEquals(AppStatusCode.MAINTENANCE_WINDOW.getMessage(), responseDto.getMessage());
    }

    @Test
    void checkAvailabilityForBookingNoRoomAvailableTest() {
        TimeDurationRequestDto timeDurationRequestDto = new TimeDurationRequestDto("10:00", "10:15");
        MeetingRoomEntity meetingRoomEntity = new MeetingRoomEntity();
        meetingRoomEntity.setId(1L);
        RoomBookingEntity roomBookingEntity = new RoomBookingEntity();
        roomBookingEntity.setRoomId(1L);
        Mockito.when(maintenanceService.checkForMaintenanceWindow(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        Mockito.when(meetingRoomRepository.findAll()).thenReturn(List.of(meetingRoomEntity));
        Mockito.when(roomBookingRepository.findByBookingDateAndStartMinuteLessThanAndEndMinuteGreaterThan(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(roomBookingEntity));
        ResponseDto<Object> responseDto = bookingService.checkAvailabilityForBooking(timeDurationRequestDto);
        Assertions.assertEquals(AppStatusCode.NO_ROOM_AVAILABLE.name(), responseDto.getStatusCode());
        Assertions.assertEquals(AppStatusCode.NO_ROOM_AVAILABLE.getMessage(), responseDto.getMessage());
    }

    @Test
    void checkAvailabilityForBookingSuccessTest() {
        TimeDurationRequestDto timeDurationRequestDto = new TimeDurationRequestDto("10:00", "10:15");
        MeetingRoomEntity meetingRoomEntity = new MeetingRoomEntity();
        meetingRoomEntity.setId(1L);
        Mockito.when(maintenanceService.checkForMaintenanceWindow(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        Mockito.when(meetingRoomRepository.findAll()).thenReturn(List.of(meetingRoomEntity));
        Mockito.when(roomBookingRepository.findByBookingDateAndStartMinuteLessThanAndEndMinuteGreaterThan(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());
        ResponseDto<Object> responseDto = bookingService.checkAvailabilityForBooking(timeDurationRequestDto);
        Assertions.assertEquals(AppStatusCode.SUCCESS.name(), responseDto.getStatusCode());
        Assertions.assertEquals(AppStatusCode.SUCCESS.getMessage(), responseDto.getMessage());
    }

    @Test
    void bookMeetingRoomMaintenanceWindowTest() {
        RoomBookingRequestDto bookingRequestDto = new RoomBookingRequestDto(5L, "test");
        bookingRequestDto.setStartTime("10:00");
        bookingRequestDto.setEndTime("10:15");
        Mockito.when(maintenanceService.checkForMaintenanceWindow(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        AppException appException = Assertions.assertThrows(AppException.class, () -> bookingService.bookMeetingRoom(bookingRequestDto));
        Assertions.assertEquals(AppStatusCode.MAINTENANCE_WINDOW.name(), appException.getStatus());
    }

    @Test
    void bookMeetingRoomCapacityExceededTest() {
        RoomBookingRequestDto bookingRequestDto = new RoomBookingRequestDto(50L, "test");
        bookingRequestDto.setStartTime("10:00");
        bookingRequestDto.setEndTime("10:15");
        Mockito.when(maintenanceService.checkForMaintenanceWindow(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        Mockito.when(meetingRoomRepository.findByCapacityGreaterThanEqual(Mockito.anyLong())).thenReturn(Collections.emptyList());
        AppException appException = Assertions.assertThrows(AppException.class, () -> bookingService.bookMeetingRoom(bookingRequestDto));
        Assertions.assertEquals(AppStatusCode.CAPACITY_EXCEEDED.name(), appException.getStatus());
    }

    @Test
    void bookMeetingRoomNoRoomAvailableTest() {
        RoomBookingRequestDto bookingRequestDto = new RoomBookingRequestDto(5L, "test");
        bookingRequestDto.setStartTime("10:00");
        bookingRequestDto.setEndTime("10:15");
        MeetingRoomEntity meetingRoomEntity = new MeetingRoomEntity();
        meetingRoomEntity.setId(1L);
        Mockito.when(maintenanceService.checkForMaintenanceWindow(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        Mockito.when(meetingRoomRepository.findByCapacityGreaterThanEqual(Mockito.anyLong())).thenReturn(List.of(meetingRoomEntity));
        Mockito.when(roomBookingProcessor.bookMeetingRoom(Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);
        AppException appException = Assertions.assertThrows(AppException.class, () -> bookingService.bookMeetingRoom(bookingRequestDto));
        Assertions.assertEquals(AppStatusCode.NO_ROOM_AVAILABLE.name(), appException.getStatus());
    }

    @Test
    void bookMeetingRoomSuccessTest() {
        RoomBookingRequestDto bookingRequestDto = new RoomBookingRequestDto(5L, "test");
        bookingRequestDto.setStartTime("10:00");
        bookingRequestDto.setEndTime("10:15");
        MeetingRoomEntity meetingRoomEntity = new MeetingRoomEntity();
        meetingRoomEntity.setId(1L);
        RoomBookingEntity roomBookingEntity = new RoomBookingEntity();
        roomBookingEntity.setId(1L);
        Pair<MeetingRoomEntity, RoomBookingEntity> bookingDetails = new Pair<>(meetingRoomEntity, roomBookingEntity);
        Mockito.when(maintenanceService.checkForMaintenanceWindow(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        Mockito.when(meetingRoomRepository.findByCapacityGreaterThanEqual(Mockito.anyLong())).thenReturn(List.of(meetingRoomEntity));
        Mockito.when(roomBookingProcessor.bookMeetingRoom(Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(bookingDetails);
        ResponseDto<Object> responseDto = Assertions.assertDoesNotThrow(() -> bookingService.bookMeetingRoom(bookingRequestDto));
        Assertions.assertEquals(AppStatusCode.SUCCESS.name(), responseDto.getStatusCode());
    }
}