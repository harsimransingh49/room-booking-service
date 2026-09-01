package com.room.booking.application;

import com.room.booking.domain.model.Booking;
import com.room.booking.domain.model.CapacityExceededException;
import com.room.booking.domain.model.MaintenanceWindowException;
import com.room.booking.domain.model.MeetingRoom;
import com.room.booking.domain.model.NoRoomAvailableException;
import com.room.booking.domain.model.TimeRange;
import com.room.booking.domain.port.BookingRepository;
import com.room.booking.domain.port.MaintenanceWindowRepository;
import com.room.booking.domain.port.MeetingRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-08-31T08:00:00Z"), ZoneOffset.UTC);

    private InMemoryRooms rooms;
    private InMemoryBookings bookings;
    private InMemoryMaintenance maintenance;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        rooms = new InMemoryRooms();
        bookings = new InMemoryBookings();
        maintenance = new InMemoryMaintenance();
        TransactionTemplate tx = mock(TransactionTemplate.class);
        when(tx.execute(any())).thenAnswer(invocation -> {
            TransactionCallback<?> callback = invocation.getArgument(0);
            return callback.doInTransaction(null);
        });
        bookingService = new BookingService(rooms, bookings, maintenance, tx, CLOCK);
    }

    @Test
    void booksSmallestRoomThatFits() {
        Booking booked = bookingService.book(command("alice", 5, "10:00", "10:30", "k1"));

        assertThat(booked.room().name()).isEqualTo("Beauty");
        assertThat(booked.room().capacity()).isEqualTo(7);
        assertThat(booked.date()).isEqualTo(LocalDate.of(2026, 8, 31));
        assertThat(bookings.saved).hasSize(1);
    }

    @Test
    void secondBookingTakesNextRoom() {
        bookingService.book(command("alice", 2, "10:00", "10:30", "k1"));
        Booking second = bookingService.book(command("bob", 2, "10:00", "10:30", "k2"));

        assertThat(second.room().name()).isEqualTo("Beauty");
    }

    @Test
    void idempotentReplayReturnsExisting() {
        Booking first = bookingService.book(command("alice", 3, "11:00", "11:15", "same"));
        Booking replay = bookingService.book(command("alice", 3, "11:00", "11:15", "same"));

        assertThat(replay.id()).isEqualTo(first.id());
        assertThat(bookings.saved).hasSize(1);
    }

    @Test
    void rejectsMaintenanceWindow() {
        assertThatThrownBy(() -> bookingService.book(command("alice", 2, "09:00", "09:15", null)))
                .isInstanceOf(MaintenanceWindowException.class);
    }

    @Test
    void rejectsWhenCapacityExceedsEveryRoom() {
        assertThatThrownBy(() -> bookingService.book(command("alice", 50, "10:00", "10:15", null)))
                .isInstanceOf(CapacityExceededException.class);
    }

    @Test
    void rejectsWhenAllFittingRoomsAreTaken() {
        bookingService.book(command("a", 20, "16:00", "16:30", "s1"));
        assertThatThrownBy(() -> bookingService.book(command("b", 20, "16:00", "16:30", "s2")))
                .isInstanceOf(NoRoomAvailableException.class);
    }

    @Test
    void rejectsPastTimeOnToday() {
        assertThatThrownBy(() -> bookingService.book(command("alice", 2, "07:00", "07:15", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already started");
    }

    @Test
    void availabilityExcludesBookedAndTooSmallRooms() {
        bookingService.book(command("alice", 3, "14:00", "14:15", "k"));

        List<MeetingRoom> free = bookingService.available(new AvailabilityQuery("14:00", "14:15", null, 7));

        assertThat(free).extracting(MeetingRoom::name).containsExactly("Beauty", "Inspire", "Strive");
    }

    @Test
    void availabilityDuringMaintenanceIsConflict() {
        assertThatThrownBy(() -> bookingService.available(new AvailabilityQuery("13:00", "13:15", null, 2)))
                .isInstanceOf(MaintenanceWindowException.class);
    }

    private static BookRoomCommand command(String user, int people, String start, String end, String key) {
        return new BookRoomCommand(user, people, start, end, null, key);
    }

    private static final class InMemoryRooms implements MeetingRoomRepository {
        private final List<MeetingRoom> rooms = List.of(
                new MeetingRoom(1L, "Amaze", 3),
                new MeetingRoom(2L, "Beauty", 7),
                new MeetingRoom(3L, "Inspire", 12),
                new MeetingRoom(4L, "Strive", 20)
        );

        @Override
        public List<MeetingRoom> findAll() {
            return rooms;
        }

        @Override
        public List<MeetingRoom> lockByMinimumCapacity(int people) {
            return rooms.stream()
                    .filter(room -> room.canHost(people))
                    .sorted(Comparator.comparingInt(MeetingRoom::capacity).thenComparing(MeetingRoom::id))
                    .toList();
        }
    }

    private static final class InMemoryBookings implements BookingRepository {
        private final Map<UUID, Booking> byId = new HashMap<>();
        private final List<Booking> saved = new ArrayList<>();

        @Override
        public Booking save(Booking booking) {
            byId.put(booking.id(), booking);
            saved.add(booking);
            return booking;
        }

        @Override
        public Optional<Booking> findById(UUID id) {
            return Optional.ofNullable(byId.get(id));
        }

        @Override
        public Optional<Booking> findByIdempotencyKey(String idempotencyKey) {
            return saved.stream().filter(booking -> idempotencyKey.equals(booking.idempotencyKey())).findFirst();
        }

        @Override
        public List<Booking> findByDate(LocalDate date) {
            return saved.stream().filter(booking -> booking.date().equals(date)).toList();
        }

        @Override
        public Set<Long> findOverlappingRoomIds(LocalDate date, TimeRange time) {
            return saved.stream()
                    .filter(booking -> booking.date().equals(date) && booking.time().overlaps(time))
                    .map(booking -> booking.room().id())
                    .collect(Collectors.toSet());
        }
    }

    private static final class InMemoryMaintenance implements MaintenanceWindowRepository {
        @Override
        public boolean overlaps(TimeRange time) {
            return time.overlaps(new TimeRange(540, 555))
                    || time.overlaps(new TimeRange(780, 795))
                    || time.overlaps(new TimeRange(1020, 1035));
        }
    }
}
