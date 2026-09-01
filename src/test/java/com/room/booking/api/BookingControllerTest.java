package com.room.booking.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.room.booking.api.dto.BookRoomRequest;
import com.room.booking.application.BookingService;
import com.room.booking.domain.model.Booking;
import com.room.booking.domain.model.MeetingRoom;
import com.room.booking.domain.model.NoRoomAvailableException;
import com.room.booking.domain.model.TimeRange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;

    @Test
    void bookValidatesPeople() throws Exception {
        BookRoomRequest request = new BookRoomRequest("alice", 1, "10:00", "10:15", null, null);
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bookReturnsCreated() throws Exception {
        UUID id = UUID.randomUUID();
        when(bookingService.book(any())).thenReturn(booking(id, "Beauty", 7));

        BookRoomRequest request = new BookRoomRequest("alice", 5, "10:00", "10:30", LocalDate.of(2026, 8, 31), "k1");
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.roomName").value("Beauty"))
                .andExpect(jsonPath("$.startTime").value("10:00"))
                .andExpect(jsonPath("$.endTime").value("10:30"));
    }

    @Test
    void bookConflictReturnsProblemDetail() throws Exception {
        when(bookingService.book(any())).thenThrow(
                new NoRoomAvailableException(LocalDate.of(2026, 8, 31), TimeRange.ofClock("10:00", "10:15")));

        BookRoomRequest request = new BookRoomRequest("alice", 5, "10:00", "10:15", null, null);
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Booking conflict"));
    }

    @Test
    void availableReturnsRooms() throws Exception {
        when(bookingService.available(any())).thenReturn(List.of(new MeetingRoom(1L, "Amaze", 3)));

        mockMvc.perform(get("/api/v1/rooms/available")
                        .param("startTime", "14:00")
                        .param("endTime", "14:15")
                        .param("people", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Amaze"));
    }

    private static Booking booking(UUID id, String roomName, int capacity) {
        return new Booking(
                id,
                new MeetingRoom(2L, roomName, capacity),
                "alice",
                5,
                LocalDate.of(2026, 8, 31),
                TimeRange.ofClock("10:00", "10:30"),
                "k1",
                Instant.parse("2026-08-31T08:00:00Z")
        );
    }
}
