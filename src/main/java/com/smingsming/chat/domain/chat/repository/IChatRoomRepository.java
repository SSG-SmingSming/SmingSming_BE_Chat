package com.smingsming.chat.domain.chat.repository;

import com.smingsming.chat.domain.chat.entity.ChatRoom;
import com.smingsming.chat.domain.chat.entity.ChatRoom3;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IChatRoomRepository extends JpaRepository<ChatRoom3, String> {
    Page<ChatRoom3> findAll(Pageable page);

    Page<ChatRoom3> findAllByNameContains(String keyword, Pageable page);

}
