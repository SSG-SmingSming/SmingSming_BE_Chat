package com.smingsming.chat.domain.chat.entity;


import lombok.Getter;
import lombok.Setter;
//import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
//@RedisHash("message")
public class ChatMessage {

    // 메시지 타입 : 입장, 퇴장, 채팅
    public enum MessageType {
        ENTER, QUIT, TALK
    }

    private String roomId;
    private String userId;
    private String contents;
    private MessageType chatType;

//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)class
    String sendTime;

}
