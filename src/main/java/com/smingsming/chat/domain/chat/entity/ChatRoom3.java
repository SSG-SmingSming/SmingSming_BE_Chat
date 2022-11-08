package com.smingsming.chat.domain.chat.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Getter
@Setter
@Data
@Entity
public class ChatRoom3 {
//@Entity
//public class ChatRoom {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
    @Id
    @Column(length = 100)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;

    private String password;
    private boolean isLock;
    private LocalDateTime createDate;
    private String userId;
    private Long playlistId;

}
