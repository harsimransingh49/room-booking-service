package com.room.booking.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class TimeDurationEntity {

    @Column(name = "START_MINUTE", nullable = false)
    private Integer startMinute;

    @Column(name = "END_MINUTE", nullable = false)
    private Integer endMinute;
}
