package com.room.booking.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "ROOM_BOOKING", indexes = {
        @Index(name = "ROOM_BOOKING_DATE_INDEX", columnList = "BOOKING_DATE")})
public class RoomBookingEntity extends TimeDurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REQUEST_ID", nullable = false)
    private String requestId;

    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    @Column(name = "BOOKING_DATE", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "ROOM_ID", nullable = false)
    private Long roomId;

}
