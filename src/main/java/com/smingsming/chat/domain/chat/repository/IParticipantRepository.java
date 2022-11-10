package com.smingsming.chat.domain.chat.repository;

import com.smingsming.chat.domain.chat.entity.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IParticipantRepository extends JpaRepository<Participant, Long> {
    void deleteByUserIdAndChatRoomId(String userId, String roomId);
    int countByChatRoomId(String roomId);
    boolean existsByChatRoomIdAndUserId(String roomId, String userId);
    void deleteByChatRoomId(String roomId);

    @Query(value = "select distinct p.userId from Participant p where p.chatRoom.id = :roomId")
    List<String> getParticipant(@Param(value = "roomId") String roomId);

    @Query(value = "select count(distinct p.userId) from Participant p where p.chatRoom.id = :roomId")
    Long countParticipantByRoomId(@Param(value = "roomId") String roomId);

}
