package com.room.booking.model.validator;

import com.room.booking.constants.AppConstants;
import com.room.booking.model.dto.TimeDurationRequestDto;
import com.room.booking.util.CommonUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class TimeDurationValidator implements
        ConstraintValidator<ValidTimeDuration, TimeDurationRequestDto> {

    @Override
    public void initialize(ValidTimeDuration constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TimeDurationRequestDto timeDurationRequestDto, ConstraintValidatorContext context) {
        if (!Pattern.matches(AppConstants.TIME_REGEX, timeDurationRequestDto.getStartTime()) ||
                !Pattern.matches(AppConstants.TIME_REGEX, timeDurationRequestDto.getEndTime())) {
            return false;
        }
        int startMinute = CommonUtil.getTimeInMinutes(timeDurationRequestDto.getStartTime());
        int endMinute = CommonUtil.getTimeInMinutes(timeDurationRequestDto.getEndTime());

        return startMinute < endMinute && startMinute % 15 == 0 && endMinute % 15 == 0;

    }
}

