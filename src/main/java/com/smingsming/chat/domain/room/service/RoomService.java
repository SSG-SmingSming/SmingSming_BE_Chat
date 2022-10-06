package com.smingsming.chat.domain.room.service;

import com.smingsming.chat.domain.room.domain.Room;
import com.smingsming.chat.domain.room.dto.RequestCreateRoomDto;
import com.smingsming.chat.domain.room.dto.RequestUpdateRoomDto;
import com.smingsming.chat.domain.room.dto.ResponseRoomDto;

import java.util.List;

public interface RoomService {
    Room createRoom(RequestCreateRoomDto requestCreateRoomDto);
    boolean deleteRoom(Long userId, String id);
    ResponseRoomDto updateRoom(RequestUpdateRoomDto UpdateRoomDto);
    List<ResponseRoomDto> getAllRoom();
}
