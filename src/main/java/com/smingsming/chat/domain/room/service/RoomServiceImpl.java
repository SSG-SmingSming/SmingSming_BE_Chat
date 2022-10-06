package com.smingsming.chat.domain.room.service;

import com.smingsming.chat.domain.room.domain.Room;
import com.smingsming.chat.domain.room.dto.RequestCreateRoomDto;
import com.smingsming.chat.domain.room.dto.RequestUpdateRoomDto;
import com.smingsming.chat.domain.room.dto.ResponseRoomDto;
import com.smingsming.chat.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public Room createRoom(RequestCreateRoomDto requestCreateRoomDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Room tmp = mapper.map(requestCreateRoomDto, Room.class);
        Room room = roomRepository.save(tmp);
        return room;
    }

    @Override
    public boolean deleteRoom(Long userId, String id) {
        Optional<Room> room = roomRepository.findById(id);

        if (room.isPresent()) {
            if (!room.get().getUserId().equals(userId))
                return false;
            roomRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public ResponseRoomDto updateRoom(RequestUpdateRoomDto UpdateRoomDto) {
        return null;
    }

    @Override
    public List<ResponseRoomDto> getAllRoom() {
        return null;
    }
}
