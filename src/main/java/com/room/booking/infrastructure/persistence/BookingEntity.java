package com.room.booking.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "room_bookings")
public class BookingEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private MeetingRoomEntity room;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "people", nullable = false)
    private int people;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "start_minute", nullable = false)
    private int startMinute;

    @Column(name = "end_minute", nullable = false)
    private int endMinute;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected BookingEntity() {
    }

    public BookingEntity(
            UUID id,
            MeetingRoomEntity room,
            String userName,
            int people,
            LocalDate bookingDate,
            int startMinute,
            int endMinute,
            String idempotencyKey,
            Instant createdAt
    ) {
        this.id = id;
        this.room = room;
        this.userName = userName;
        this.people = people;
        this.bookingDate = bookingDate;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
        this.idempotencyKey = idempotencyKey;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public MeetingRoomEntity getRoom() {
        return room;
    }

    public String getUserName() {
        return userName;
    }

    public int getPeople() {
        return people;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
