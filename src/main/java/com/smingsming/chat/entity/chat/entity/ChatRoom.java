package com.smingsming.chat.entity.chat.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Data
@Entity
public class ChatRoom {
    @Id
    @Column(length = 100)
    private String id;
    private String name;

    private String password;
    private boolean isLock;
    private LocalDateTime createDate;
    private String userId;
    private Long playlistId;


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
