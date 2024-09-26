package com.room.booking.processor.impl;

import com.room.booking.helper.BookingHelper;
import com.room.booking.model.entity.BookingSlotEntity;
import com.room.booking.model.entity.MeetingRoomEntity;
import com.room.booking.model.entity.RoomBookingEntity;
import com.room.booking.processor.RoomBookingProcessor;
import com.room.booking.repository.BookingSlotRepository;
import com.room.booking.repository.RoomBookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * The processor for locking time slot and booking room
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoomBookingProcessorImpl implements RoomBookingProcessor {

    private final BookingSlotRepository bookingSlotRepository;

    private final RoomBookingRepository roomBookingRepository;

    private final BookingHelper bookingHelper;

    /**
     * @param meetingRoomEntities List of meeting room entities
     * @param requestId           The request ID
     * @param userName            The name of user requesting for booking
     * @param startMinute         The start minute for booking
     * @param endMinute           The end minute for booking
     * @return Pair of MeetingRoomEntity and RoomBookingEntity
     */
    @Override
    @Transactional
    public Pair<MeetingRoomEntity, RoomBookingEntity> bookMeetingRoom(List<MeetingRoomEntity> meetingRoomEntities, String requestId, String userName, Integer startMinute, Integer endMinute) {
        //lock the slot/slots for booking
        List<BookingSlotEntity> slots = bookingSlotRepository.findByStartMinuteLessThanAndEndMinuteGreaterThan(endMinute, startMinute);
        log.info("No. of slots locked for request ID {} : {}", requestId, slots.size());
        //check for bookings in the given time slot for the current date
        List<RoomBookingEntity> roomBookingEntities = roomBookingRepository.findByBookingDateAndStartMinuteLessThanAndEndMinuteGreaterThan(LocalDate.now(), endMinute, startMinute);
        List<MeetingRoomEntity> availableRoomEntities = meetingRoomEntities.stream().filter(room -> bookingHelper.isRoomAvailable(room, roomBookingEntities)).toList();
        if (CollectionUtils.isEmpty(availableRoomEntities)) {
            log.info("No available room for request ID {}", requestId);
            return null;
        }
        //Fetch the optimal room
        MeetingRoomEntity selectedRoom = availableRoomEntities.stream().min(Comparator.comparingLong(MeetingRoomEntity::getCapacity)).orElse(null);
        //Save the booking entity
        RoomBookingEntity roomBookingEntity = bookingHelper.createRoomBooking(selectedRoom, requestId, userName, startMinute, endMinute);
        roomBookingRepository.save(roomBookingEntity);
        log.info("Saved booking for request id {}", requestId);
        return new Pair<>(selectedRoom, roomBookingEntity);

    }
}
