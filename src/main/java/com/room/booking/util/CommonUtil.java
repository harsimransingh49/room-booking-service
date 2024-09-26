package com.room.booking.util;

import com.room.booking.constants.AppConstants;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class CommonUtil {

    public int getTimeInMinutes(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(AppConstants.TIME_FORMAT);
        LocalTime localTime = LocalTime.parse(time, dateTimeFormatter);
        return localTime.getHour() * 60 + localTime.getMinute();
    }
}
