package com.room.booking.controller;

import com.room.booking.model.dto.ResponseDto;
import com.room.booking.model.dto.RoomBookingRequestDto;
import com.room.booking.model.dto.TimeDurationRequestDto;
import com.room.booking.exception.AppException;
import com.room.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle booking requests and to check availability
 */
@RestController
@Slf4j
@RequestMapping("/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * @param timeDurationRequestDto Room Availability Request
     * @return Response Object
     */
    @Operation(summary = "Room availability check API", description = "API to check room availability by time range in HH:mm format")
    @PostMapping("/check-availability")
    public ResponseEntity<ResponseDto<Object>> checkAvailabilityForBooking(@RequestBody @Valid TimeDurationRequestDto timeDurationRequestDto) {
        log.info("Received check availability request from {} to {}", timeDurationRequestDto.getStartTime(), timeDurationRequestDto.getEndTime());
        return ResponseEntity.ok(bookingService.checkAvailabilityForBooking(timeDurationRequestDto));
    }

    /**
     * @param bookingRequestDto Booking request
     * @return Response Object
     * @throws AppException if booking is not done
     */
    @Operation(summary = "Room booking API", description = "API to book conference room by no. of people and time range in HH:mm format")
    @PostMapping("/book-room")
    public ResponseEntity<ResponseDto<Object>> bookMeetingRoom(@RequestBody @Valid RoomBookingRequestDto bookingRequestDto) throws AppException {
        log.info("Received booking request for {} people from {} to {}", bookingRequestDto.getNoOfPeople(), bookingRequestDto.getStartTime(), bookingRequestDto.getEndTime());
        return ResponseEntity.ok(bookingService.bookMeetingRoom(bookingRequestDto));
    }
}
