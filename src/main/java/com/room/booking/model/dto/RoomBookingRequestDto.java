package com.room.booking.model.dto;

import com.room.booking.constants.AppConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor()
public class RoomBookingRequestDto extends TimeDurationRequestDto {

    @NotNull
    @Min(value = AppConstants.MIN_PEOPLE, message = AppConstants.MIN_PEOPLE_MESSAGE)
    private Long noOfPeople;

    @NotBlank
    private String userName;

}
