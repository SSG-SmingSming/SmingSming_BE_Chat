package com.smingsming.chat.entity.chat.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    // 메시지 타입 : 입장, 퇴장, 채팅
    public enum MessageType {
        ENTER, QUIT, TALK
    }

    private String roomId;
    private String userId;
    private String contents;
    private MessageType chatType;

    String sendTime;

}
