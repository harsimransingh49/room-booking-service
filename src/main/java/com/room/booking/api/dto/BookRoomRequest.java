package com.room.booking.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record BookRoomRequest(
        @NotBlank @Size(max = 120) String userName,
        @NotNull @Min(2) Integer people,
        @NotBlank @Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message = "must be HH:mm") String startTime,
        @NotBlank @Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message = "must be HH:mm") String endTime,
        LocalDate date,
        @Size(max = 128) String idempotencyKey
) {
}
