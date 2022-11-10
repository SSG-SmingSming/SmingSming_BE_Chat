package com.smingsming.chat.entity.chat.repository;

import com.smingsming.chat.entity.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Page<ChatRoom> findAll(Pageable page);

    Page<ChatRoom> findAllByNameContains(String keyword, Pageable page);

}
