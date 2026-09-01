package com.room.booking.infrastructure.persistence;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface SpringDataMeetingRoomRepository extends JpaRepository<MeetingRoomEntity, Long> {

    List<MeetingRoomEntity> findAllByOrderByCapacityAscIdAsc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from MeetingRoomEntity r where r.capacity >= :capacity order by r.capacity asc, r.id asc")
    List<MeetingRoomEntity> lockByMinimumCapacity(@Param("capacity") int capacity);
}
