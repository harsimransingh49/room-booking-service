package com.room.booking.model.validator;

import com.room.booking.model.dto.TimeDurationRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TimeDurationValidatorTest {

    TimeDurationValidator timeDurationValidator = new TimeDurationValidator();

    @Test
    void isValidInvalidFormatTest() {
        TimeDurationRequestDto timeDurationRequestDto = new TimeDurationRequestDto("9:00", "9:15");
        Assertions.assertFalse(timeDurationValidator.isValid(timeDurationRequestDto, null));
    }

    @Test
    void isValidInvalidTimeDurationTest() {
        TimeDurationRequestDto timeDurationRequestDto = new TimeDurationRequestDto("09:15", "9:15");
        Assertions.assertFalse(timeDurationValidator.isValid(timeDurationRequestDto, null));
    }

    @Test
    void isValidInvalidTimeTest() {
        TimeDurationRequestDto timeDurationRequestDto = new TimeDurationRequestDto("09:20", "9:35");
        Assertions.assertFalse(timeDurationValidator.isValid(timeDurationRequestDto, null));
    }
}