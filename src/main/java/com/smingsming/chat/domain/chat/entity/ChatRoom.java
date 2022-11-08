package com.smingsming.chat.domain.chat.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Data
@RedisHash("chatroom")
public class ChatRoom implements Serializable {
//@Entity
//public class ChatRoom {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;
    private String name;

    private String password;
    private boolean isLock;
    private LocalDateTime createDate;
    private String userId;
    private Long playlistId;

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

    public static ChatRoom create(String name, String password, boolean isLock,
                    String userId, Long playlistId) {

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = UUID.randomUUID().toString();
        chatRoom.name = name;
        chatRoom.isLock = isLock;
        chatRoom.password = password;
        chatRoom.userId = userId;
        chatRoom.playlistId = playlistId;
        chatRoom.createDate = LocalDateTime.now();

        return chatRoom;
    }
}
