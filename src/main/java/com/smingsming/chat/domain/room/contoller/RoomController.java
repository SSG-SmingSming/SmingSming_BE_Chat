package com.smingsming.chat.domain.room.contoller;

import com.smingsming.chat.domain.room.domain.Room;
import com.smingsming.chat.domain.room.dto.RequestCreateRoomDto;
import com.smingsming.chat.domain.room.dto.ResponseRoomDto;
import com.smingsming.chat.domain.room.repository.RoomRepository;
import com.smingsming.chat.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/chat-server/room/")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;

    @PostMapping("/add")
    public ResponseEntity<?> createRoom(@RequestBody RequestCreateRoomDto requestCreateRoomDto) {

        Room room = roomService.createRoom(requestCreateRoomDto);

        if(room != null){
            ResponseEntity tmp =  ResponseEntity.status(HttpStatus.OK).body(new ModelMapper().map(room, ResponseRoomDto.class));
            return tmp;
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("생성 실패");
    }

    @DeleteMapping("/test/{userId}/{roomId}")
    public ResponseEntity<?> delete(@PathVariable("userId") Long userId, @PathVariable("roomId") String roomId) {
        boolean result = roomService.deleteRoom(userId, roomId);

        if(result)
            return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
        else
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("삭제 실패");

    }

}
