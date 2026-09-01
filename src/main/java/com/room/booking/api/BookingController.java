package com.room.booking.api;

import com.room.booking.api.dto.BookRoomRequest;
import com.room.booking.api.dto.BookingResponse;
import com.room.booking.api.dto.RoomResponse;
import com.room.booking.application.AvailabilityQuery;
import com.room.booking.application.BookRoomCommand;
import com.room.booking.application.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Room Booking")
public class BookingController {

    private static final String TIME = "([01][0-9]|2[0-3]):[0-5][0-9]";

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Book the smallest room that fits the party")
    public BookingResponse book(@Valid @RequestBody BookRoomRequest request) {
        return BookingResponse.from(bookingService.book(new BookRoomCommand(
                request.userName(),
                request.people(),
                request.startTime(),
                request.endTime(),
                request.date(),
                request.idempotencyKey()
        )));
    }

    @GetMapping("/bookings/{id}")
    @Operation(summary = "Get a booking")
    public BookingResponse get(@PathVariable UUID id) {
        return BookingResponse.from(bookingService.get(id));
    }

    @GetMapping("/bookings")
    @Operation(summary = "List bookings for a date (defaults to today)")
    public List<BookingResponse> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return bookingService.listByDate(date).stream().map(BookingResponse::from).toList();
    }

    @GetMapping("/rooms")
    @Operation(summary = "List all meeting rooms")
    public List<RoomResponse> rooms() {
        return bookingService.rooms().stream().map(RoomResponse::from).toList();
    }

    @GetMapping("/rooms/available")
    @Operation(summary = "List rooms free for a 15-minute-aligned interval")
    public List<RoomResponse> available(
            @RequestParam @Pattern(regexp = TIME, message = "must be HH:mm") String startTime,
            @RequestParam @Pattern(regexp = TIME, message = "must be HH:mm") String endTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @Min(2) Integer people
    ) {
        return bookingService.available(new AvailabilityQuery(startTime, endTime, date, people)).stream()
                .map(RoomResponse::from)
                .toList();
    }
}
