package com.room.booking.application;

import com.room.booking.domain.model.Booking;
import com.room.booking.domain.model.BookingNotFoundException;
import com.room.booking.domain.model.CapacityExceededException;
import com.room.booking.domain.model.MaintenanceWindowException;
import com.room.booking.domain.model.MeetingRoom;
import com.room.booking.domain.model.NoRoomAvailableException;
import com.room.booking.domain.model.TimeRange;
import com.room.booking.domain.port.BookingRepository;
import com.room.booking.domain.port.MaintenanceWindowRepository;
import com.room.booking.domain.port.MeetingRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Allocates the smallest room that fits. Money-equivalent here is inventory:
 * rooms are locked, then overlap is re-checked, then the booking is written.
 */
@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final MeetingRoomRepository meetingRoomRepository;
    private final BookingRepository bookingRepository;
    private final MaintenanceWindowRepository maintenanceWindowRepository;
    private final TransactionTemplate transactionTemplate;
    private final Clock clock;

    public BookingService(
            MeetingRoomRepository meetingRoomRepository,
            BookingRepository bookingRepository,
            MaintenanceWindowRepository maintenanceWindowRepository,
            TransactionTemplate transactionTemplate,
            Clock clock
    ) {
        this.meetingRoomRepository = meetingRoomRepository;
        this.bookingRepository = bookingRepository;
        this.maintenanceWindowRepository = maintenanceWindowRepository;
        this.transactionTemplate = transactionTemplate;
        this.clock = clock;
    }

    public Booking book(BookRoomCommand command) {
        TimeRange time = TimeRange.ofClock(command.startTime(), command.endTime());
        LocalDate date = resolveDate(command.date());
        rejectPast(date, time);
        if (command.people() < Booking.MIN_PEOPLE) {
            throw new IllegalArgumentException("At least " + Booking.MIN_PEOPLE + " people are required");
        }
        String userName = requireUserName(command.userName());
        String idempotencyKey = blankToNull(command.idempotencyKey());
        if (idempotencyKey != null) {
            var existing = bookingRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        Booking booked = Objects.requireNonNull(transactionTemplate.execute(status ->
                allocate(userName, command.people(), date, time, idempotencyKey)
        ));
        log.info("Booked {} for {} people on {} {}-{} ({})",
                booked.room().name(), booked.people(), booked.date(),
                booked.time().startClock(), booked.time().endClock(), booked.id());
        return booked;
    }

    public List<MeetingRoom> available(AvailabilityQuery query) {
        TimeRange time = TimeRange.ofClock(query.startTime(), query.endTime());
        LocalDate date = resolveDate(query.date());
        int people = query.people() == null ? Booking.MIN_PEOPLE : query.people();
        if (people < Booking.MIN_PEOPLE) {
            throw new IllegalArgumentException("At least " + Booking.MIN_PEOPLE + " people are required");
        }
        if (maintenanceWindowRepository.overlaps(time)) {
            throw new MaintenanceWindowException();
        }
        Set<Long> busy = bookingRepository.findOverlappingRoomIds(date, time);
        return meetingRoomRepository.findAll().stream()
                .filter(room -> room.canHost(people))
                .filter(room -> !busy.contains(room.id()))
                .toList();
    }

    public List<MeetingRoom> rooms() {
        return meetingRoomRepository.findAll();
    }

    public Booking get(UUID id) {
        return bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
    }

    public List<Booking> listByDate(LocalDate date) {
        return bookingRepository.findByDate(resolveDate(date));
    }

    private Booking allocate(String userName, int people, LocalDate date, TimeRange time, String idempotencyKey) {
        if (maintenanceWindowRepository.overlaps(time)) {
            throw new MaintenanceWindowException();
        }
        List<MeetingRoom> candidates = meetingRoomRepository.lockByMinimumCapacity(people);
        if (candidates.isEmpty()) {
            throw new CapacityExceededException(people);
        }
        Set<Long> busy = bookingRepository.findOverlappingRoomIds(date, time);
        MeetingRoom selected = candidates.stream()
                .filter(room -> !busy.contains(room.id()))
                .findFirst()
                .orElseThrow(() -> new NoRoomAvailableException(date, time));
        return bookingRepository.save(new Booking(
                UUID.randomUUID(),
                selected,
                userName,
                people,
                date,
                time,
                idempotencyKey,
                Instant.now(clock)
        ));
    }

    private LocalDate resolveDate(LocalDate date) {
        return date == null ? LocalDate.now(clock) : date;
    }

    private void rejectPast(LocalDate date, TimeRange time) {
        LocalDate today = LocalDate.now(clock);
        if (date.isBefore(today)) {
            throw new IllegalArgumentException("Cannot book a date in the past");
        }
        if (date.equals(today)) {
            LocalTime now = LocalTime.now(clock);
            int nowMinute = now.getHour() * 60 + now.getMinute();
            if (time.startMinute() < nowMinute) {
                throw new IllegalArgumentException("Cannot book a time that has already started");
            }
        }
    }

    private static String requireUserName(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("userName is required");
        }
        return userName.trim();
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
