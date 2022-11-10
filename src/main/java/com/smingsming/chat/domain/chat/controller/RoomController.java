package com.smingsming.chat.domain.chat.controller;

import com.smingsming.chat.domain.chat.entity.ChatRoom;
import com.smingsming.chat.domain.chat.service.IChatRoomService;
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

    // 채팅방 생성
    @PostMapping("/add")
    public ResponseEntity<?> createRoom(@RequestBody RoomAddReqVo roomAddReqVo, HttpServletRequest request) {
        ChatRoom result = iChatRoomService.createChatRoom(roomAddReqVo, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 채팅방 입장
    @PostMapping("/enter")
    public ResponseEntity<?> enterRoom(@RequestBody RoomEnterReqVo enterReqVo, HttpServletRequest request) {

        ChatRoom result = iChatRoomService.enterRoom(enterReqVo, request);

        if (result != null)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        else
            return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    // 채팅방 참가자 목록
    @GetMapping("/get/participant/{roomId}")
    public ResponseEntity<?> getRoomParticipant(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @PathVariable String roomId, HttpServletRequest request) {
        List<String> result = iChatRoomService.getParticipant(page, roomId, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 채팅방 목록 조회
    @GetMapping("/get")
    public ResponseEntity<?> getRoomList(@RequestParam(value = "page", defaultValue = "1") int page) {
        return ResponseEntity.status(HttpStatus.OK).body(iChatRoomService.findAllChatRoom(page));
    }

    // 채팅방 정보 조회
    @GetMapping("/info/{roomId}")
    @ResponseBody
    public ResponseEntity<?> roomInfo(@PathVariable String roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ModelMapper().map(iChatRoomService.findById(roomId), RoomVo.class));
    }

    // 채팅방 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchRoom(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(iChatRoomService.searchRoom(keyword, page, request));
    }

}
