package com.room.booking.service;

import com.room.booking.model.dto.ResponseDto;
import com.room.booking.model.dto.RoomBookingRequestDto;
import com.room.booking.model.dto.TimeDurationRequestDto;
import com.room.booking.exception.AppException;

public interface BookingService {

    ResponseDto<Object> checkAvailabilityForBooking(TimeDurationRequestDto timeDurationRequestDto);

    ResponseDto<Object> bookMeetingRoom(RoomBookingRequestDto bookingRequestDto) throws AppException;
}
