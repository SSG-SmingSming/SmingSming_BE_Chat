package com.smingsming.chat.entity.chat.controller;

import com.smingsming.chat.entity.chat.entity.ChatMessage;
import com.smingsming.chat.entity.chat.service.IChatRoomService;
import com.smingsming.chat.global.pubsub.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final IChatRoomService iChatRoomService;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        message.setSendTime(DateTimeFormatter.ofPattern("HH:mm").format(LocalDateTime.now()));

        if (ChatMessage.MessageType.ENTER.equals(message.getChatType()))
            iChatRoomService.enterChatRoom(message.getRoomId(), message.getUserId());
        else if (ChatMessage.MessageType.QUIT.equals(message.getChatType()))
            iChatRoomService.quitRoom(message.getRoomId(), message.getUserId());

//            message.setContents(message.getRoomId() + "님이 입장하셨습니다.");
        redisPublisher.publish(iChatRoomService.getTopic(message.getRoomId()), message);
    }
}
