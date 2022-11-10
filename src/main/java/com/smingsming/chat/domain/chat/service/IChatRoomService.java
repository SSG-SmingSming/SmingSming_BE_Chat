package com.smingsming.chat.domain.chat.service;

import com.smingsming.chat.domain.chat.entity.ChatRoom;
import com.smingsming.chat.domain.chat.vo.*;
import org.springframework.data.redis.listener.ChannelTopic;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IChatRoomService {
    void quitRoom(String roomId, String userId);
    ChatRoom createChatRoom(RoomAddReqVo roomAddReqVo, HttpServletRequest request);
    RoomVo findById(String roomId);
    void enterChatRoom(String roomId, String userId);
    ChannelTopic getTopic(String roomId);
    List<RoomVo> findAllChatRoom(int page);
    ChatRoom enterRoom(RoomEnterReqVo reqVo, HttpServletRequest request);
    List<String> getParticipant(int page, String roomId, HttpServletRequest request);
    RoomSearchVo searchRoom(String keyword, int page, HttpServletRequest request);
}
