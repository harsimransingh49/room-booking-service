package com.room.booking.model.dto;

import com.room.booking.constants.AppConstants;
import com.room.booking.model.validator.ValidTimeDuration;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidTimeDuration
public class TimeDurationRequestDto {

    @NotBlank
    @Pattern(regexp = AppConstants.TIME_REGEX, message = AppConstants.INVALID_TIME_FORMAT_MESSAGE)
    private String startTime;

    @NotBlank
    @Pattern(regexp = AppConstants.TIME_REGEX, message = AppConstants.INVALID_TIME_FORMAT_MESSAGE)
    private String endTime;


}
