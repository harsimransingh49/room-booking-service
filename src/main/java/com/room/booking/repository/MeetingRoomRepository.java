package com.room.booking.repository;

import com.room.booking.model.entity.MeetingRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoomEntity, Long> {

    List<MeetingRoomEntity> findByCapacityGreaterThanEqual(long capacity);
}
