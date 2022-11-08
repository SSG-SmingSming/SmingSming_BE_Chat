package com.smingsming.chat.domain.chat.controller;

import com.smingsming.chat.domain.chat.entity.ChatRoom;
import com.smingsming.chat.domain.chat.entity.ChatRoom3;
import com.smingsming.chat.domain.chat.entity.Participant;
import com.smingsming.chat.domain.chat.service.IChatRoomService;
import com.smingsming.chat.domain.chat.vo.ParticipantVo;
import com.smingsming.chat.domain.chat.vo.RoomAddReqVo;
import com.smingsming.chat.domain.chat.vo.RoomEnterReqVo;
import com.smingsming.chat.domain.chat.vo.RoomVo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final IChatRoomService iChatRoomService;

    @PostMapping("/add")
    public ResponseEntity<?> createRoom(@RequestBody RoomAddReqVo roomAddReqVo, HttpServletRequest request) {
        ChatRoom result = iChatRoomService.createChatRoom(roomAddReqVo, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/enter")
    public ResponseEntity<?> enterRoom(@RequestBody RoomEnterReqVo enterReqVo, HttpServletRequest request) {

        ChatRoom3 result = iChatRoomService.enterRoom(enterReqVo, request);

        if (result != null)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        else
            return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    @GetMapping("/get/participant/{roomId}")
    public ResponseEntity<?> getRoomParticipant(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @PathVariable String roomId, HttpServletRequest request) {
        List<String > result = iChatRoomService.getParticipant(page, roomId, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getRoomList(@RequestParam(value = "page", defaultValue = "1") int page) {
        return ResponseEntity.status(HttpStatus.OK).body(iChatRoomService.findAllChatRoom(page));
    }

    @GetMapping("/info/{roomId}")
    @ResponseBody
    public ResponseEntity<?> roomInfo(@PathVariable String roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ModelMapper().map(iChatRoomService.findById(roomId), RoomVo.class));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchRoom(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(iChatRoomService.searchRoom(keyword, page, request));
    }

}
