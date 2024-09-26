package com.room.booking.controller;

import com.room.booking.exception.AppException;
import com.room.booking.model.dto.ResponseDto;
import com.room.booking.model.dto.RoomBookingRequestDto;
import com.room.booking.model.dto.TimeDurationRequestDto;
import com.room.booking.service.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @InjectMocks
    BookingController bookingController;

    @Mock
    BookingService bookingService;

    @Test
    void checkAvailabilityForBookingTest() {
        TimeDurationRequestDto timeDurationRequestDto = new TimeDurationRequestDto("10:00", "10:30");
        Mockito.when(bookingService.checkAvailabilityForBooking(Mockito.any())).thenReturn(ResponseDto.builder().build());
        Assertions.assertEquals(HttpStatusCode.valueOf(200), bookingController.checkAvailabilityForBooking(timeDurationRequestDto).getStatusCode());
    }

    @Test
    void bookMeetingRoomTest() throws AppException {
        RoomBookingRequestDto bookingRequestDto = new RoomBookingRequestDto(2L, "test");
        bookingRequestDto.setStartTime("10:00");
        bookingRequestDto.setEndTime("10:30");
        Mockito.when(bookingService.bookMeetingRoom(Mockito.any())).thenReturn(ResponseDto.builder().build());
        Assertions.assertEquals(HttpStatusCode.valueOf(200), bookingController.bookMeetingRoom(bookingRequestDto).getStatusCode());
    }
}