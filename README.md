# Room Booking Service

Spring Boot service for conference rooms. It books the **smallest room that fits**, on **15-minute** boundaries. Overlapping bookings are rejected. **Rooms are locked first**, then availability is re-checked, then the booking is written. PostgreSQL also enforces a range exclusion so two writers cannot double-book the same room.

## Rooms and maintenance

| Room | Capacity |
| --- | ---: |
| Amaze | 3 |
| Beauty | 7 |
| Inspire | 12 |
| Strive | 20 |

Maintenance (all rooms): `09:00–09:15`, `13:00–13:15`, `17:00–17:15`.

Rules kept from the original service:

- At least **2** people.
- Times must be `HH:mm` on a 15-minute mark, `end` after `start`.
- Past dates and times that have already started are rejected.

What changed:

- Errors are **RFC 7807** (`409` conflict, `400` validation). They are no longer HTTP 200 envelopes.
- Dummy `BOOKING_SLOT` rows are gone. Concurrency is `SELECT … FOR UPDATE` on candidate rooms plus a Postgres `EXCLUDE` constraint.
- Bookings have a **date** (defaults to today) and an optional **idempotency key**.
- Persistence is **PostgreSQL + Flyway**, not in-memory H2.

## API

```bash
curl -s -X POST http://localhost:8080/api/v1/bookings ^
  -H "Content-Type: application/json" ^
  -d "{\"userName\":\"alice\",\"people\":5,\"startTime\":\"10:00\",\"endTime\":\"10:30\",\"date\":\"2026-08-31\",\"idempotencyKey\":\"stand-up-1\"}"
```

- `GET /api/v1/rooms/available?startTime=14:00&endTime=14:15&people=2`
- `GET /api/v1/rooms`
- `GET /api/v1/bookings/{id}`
- `GET /api/v1/bookings?date=2026-08-31`
- Swagger: http://localhost:8080/swagger-ui.html
- Postman: `postman/room-booking-service.postman_collection.json`

## Run

```bash
docker run -d --name booking-postgres -e POSTGRES_DB=booking -e POSTGRES_USER=booking -e POSTGRES_PASSWORD=booking -p 5432:5432 postgres:16-alpine
cd room-booking-service
.\gradlew bootRun
```

If 5432 is already used, create a `booking` database on that instance and set `POSTGRES_URL=jdbc:postgresql://localhost:5432/booking`.
