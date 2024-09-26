package com.room.booking.model.mapper;

import com.room.booking.model.dto.BookedRoomDto;
import com.room.booking.model.dto.MeetingRoomDto;
import com.room.booking.model.entity.MeetingRoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MeetingRoomMapper {

    MeetingRoomMapper INSTANCE = Mappers.getMapper(MeetingRoomMapper.class);

    List<MeetingRoomDto> mapMeetingRoomListEntityToDto(List<MeetingRoomEntity> meetingRoomEntities);

    BookedRoomDto mapMeetingRoomEntityToBookedRoomDto(MeetingRoomEntity meetingRoomEntity);
}
