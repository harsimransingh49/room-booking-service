package com.room.booking.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommonUtilTest {

    @Test
    void getTimeInMinutes() {
        int minutes = CommonUtil.getTimeInMinutes("00:15");
        Assertions.assertEquals(15, minutes);
    }
}