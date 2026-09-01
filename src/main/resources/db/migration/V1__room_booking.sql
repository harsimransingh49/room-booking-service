CREATE TABLE meeting_rooms (
    id BIGINT PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    capacity INT NOT NULL CHECK (capacity > 0)
);

CREATE TABLE maintenance_windows (
    id BIGINT PRIMARY KEY,
    start_minute INT NOT NULL,
    end_minute INT NOT NULL,
    CONSTRAINT ck_maintenance_range CHECK (start_minute < end_minute)
);

CREATE TABLE room_bookings (
    id UUID PRIMARY KEY,
    room_id BIGINT NOT NULL REFERENCES meeting_rooms (id),
    user_name VARCHAR(120) NOT NULL,
    people INT NOT NULL CHECK (people >= 2),
    booking_date DATE NOT NULL,
    start_minute INT NOT NULL,
    end_minute INT NOT NULL,
    idempotency_key VARCHAR(128),
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT ck_booking_range CHECK (start_minute < end_minute),
    CONSTRAINT ck_booking_slot CHECK (start_minute % 15 = 0 AND end_minute % 15 = 0)
);

CREATE UNIQUE INDEX uk_room_bookings_idempotency
    ON room_bookings (idempotency_key)
    WHERE idempotency_key IS NOT NULL;

CREATE INDEX idx_room_bookings_date_room ON room_bookings (booking_date, room_id);
CREATE INDEX idx_room_bookings_overlap ON room_bookings (booking_date, start_minute, end_minute);

CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE room_bookings ADD CONSTRAINT room_bookings_no_overlap
    EXCLUDE USING gist (
        room_id WITH =,
        booking_date WITH =,
        int4range(start_minute, end_minute, '[)') WITH &&
    );

INSERT INTO meeting_rooms (id, name, capacity) VALUES
    (1, 'Amaze', 3),
    (2, 'Beauty', 7),
    (3, 'Inspire', 12),
    (4, 'Strive', 20);

INSERT INTO maintenance_windows (id, start_minute, end_minute) VALUES
    (1, 540, 555),
    (2, 780, 795),
    (3, 1020, 1035);
