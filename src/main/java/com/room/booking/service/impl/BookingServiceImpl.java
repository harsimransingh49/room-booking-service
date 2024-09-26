package com.room.booking.service.impl;

import com.room.booking.model.dto.*;
import com.room.booking.model.entity.MeetingRoomEntity;
import com.room.booking.model.entity.RoomBookingEntity;
import com.room.booking.model.enums.AppStatusCode;
import com.room.booking.exception.AppException;
import com.room.booking.helper.BookingHelper;
import com.room.booking.model.mapper.MeetingRoomMapper;
import com.room.booking.processor.RoomBookingProcessor;
import com.room.booking.repository.MeetingRoomRepository;
import com.room.booking.repository.RoomBookingRepository;
import com.room.booking.service.BookingService;
import com.room.booking.service.MaintenanceService;
import com.room.booking.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service to handle bookings and to check availability
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final MaintenanceService maintenanceService;

    private final RoomBookingRepository roomBookingRepository;

    private final MeetingRoomRepository meetingRoomRepository;

    private final RoomBookingProcessor roomBookingProcessor;

    private final BookingHelper bookingHelper;

    /**
     * @param timeDurationRequestDto The booking duration
     * @return Response Object
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseDto<Object> checkAvailabilityForBooking(TimeDurationRequestDto timeDurationRequestDto) {
        Integer startMinute = CommonUtil.getTimeInMinutes(timeDurationRequestDto.getStartTime());
        Integer endMinute = CommonUtil.getTimeInMinutes(timeDurationRequestDto.getEndTime());
        //check for maintenance window
        if (maintenanceService.checkForMaintenanceWindow(startMinute, endMinute)) {
            log.info("Availability request in maintenance window");
            return ResponseDto.builder().statusCode(AppStatusCode.MAINTENANCE_WINDOW.name()).message(AppStatusCode.MAINTENANCE_WINDOW.getMessage()).build();
        }
        List<MeetingRoomEntity> meetingRoomEntities = meetingRoomRepository.findAll();
        //fetch all bookings for the requested time
        List<RoomBookingEntity> roomBookingEntities = roomBookingRepository.findByBookingDateAndStartMinuteLessThanAndEndMinuteGreaterThan(LocalDate.now(), endMinute, startMinute);
        List<MeetingRoomEntity> availableRoomEntities = meetingRoomEntities.stream().filter(room -> bookingHelper.isRoomAvailable(room, roomBookingEntities)).toList();
        if (CollectionUtils.isEmpty(availableRoomEntities)) {
            log.info("No available room found for the availability request");
            return ResponseDto.builder().statusCode(AppStatusCode.NO_ROOM_AVAILABLE.name()).message(AppStatusCode.NO_ROOM_AVAILABLE.getMessage()).build();
        }
        List<MeetingRoomDto> availableRoomDtoList = MeetingRoomMapper.INSTANCE.mapMeetingRoomListEntityToDto(availableRoomEntities);
        return ResponseDto.builder().statusCode(AppStatusCode.SUCCESS.name()).message(AppStatusCode.SUCCESS.getMessage()).data(availableRoomDtoList).build();
    }

    /**
     * @param bookingRequestDto Booking request
     * @return Response Object
     * @throws AppException if booking is not possible
     */
    public ResponseDto<Object> bookMeetingRoom(RoomBookingRequestDto bookingRequestDto) throws AppException {
        Integer startMinute = CommonUtil.getTimeInMinutes(bookingRequestDto.getStartTime());
        Integer endMinute = CommonUtil.getTimeInMinutes(bookingRequestDto.getEndTime());
        if (maintenanceService.checkForMaintenanceWindow(startMinute, endMinute)) {
            log.info("Booking request for user {} in maintenance window", bookingRequestDto.getUserName());
            throw new AppException(AppStatusCode.MAINTENANCE_WINDOW.name(), AppStatusCode.MAINTENANCE_WINDOW.getMessage());
        }
        List<MeetingRoomEntity> meetingRoomEntities = meetingRoomRepository.findByCapacityGreaterThanEqual(bookingRequestDto.getNoOfPeople());
        //If the desired capacity is greater than the capacity of all rooms
        if (CollectionUtils.isEmpty(meetingRoomEntities)) {
            log.info("Desired capacity {} is greater than the capacity of all rooms", bookingRequestDto.getNoOfPeople());
            throw new AppException(AppStatusCode.CAPACITY_EXCEEDED.name(), AppStatusCode.CAPACITY_EXCEEDED.getMessage());
        }
        String requestId = UUID.randomUUID().toString();
        Pair<MeetingRoomEntity, RoomBookingEntity> bookingDetails = roomBookingProcessor.bookMeetingRoom(meetingRoomEntities, requestId, bookingRequestDto.getUserName(), startMinute, endMinute);
        //if no room is available
        if (Objects.isNull(bookingDetails)) {
            log.info("No room available for booking request {}", requestId);
            throw new AppException(AppStatusCode.NO_ROOM_AVAILABLE.name(), AppStatusCode.NO_ROOM_AVAILABLE.getMessage());
        }
        BookedRoomDto bookedRoomDto = bookingHelper.createBookedRoomDto(bookingDetails, requestId);
        return ResponseDto.builder().statusCode(AppStatusCode.SUCCESS.name()).message(AppStatusCode.SUCCESS.getMessage()).data(bookedRoomDto).build();
    }

}
