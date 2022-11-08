package com.smingsming.chat.domain.chat.service;

import com.smingsming.chat.domain.chat.entity.ChatRoom;
import com.smingsming.chat.domain.chat.entity.ChatRoom3;
import com.smingsming.chat.domain.chat.entity.Participant;
import com.smingsming.chat.domain.chat.vo.*;
import com.smingsming.chat.global.vo.UserDetailVo;
import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.ChannelTopic;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IChatRoomService {
//    List<ChatRoom> findAllRoom();
//    ChatRoom createChatRoom(String name);
//    String getUserEnterRoomId(String sessionId);
//    void remoteUserEnterInfo(String sessionId);
//    void sendChatMessage(ChatMessage message);
//    boolean quitRoom(String roomId, HttpServletRequest request);
    void quitRoom(String roomId, String userId);
    ChatRoom createChatRoom(RoomAddReqVo roomAddReqVo, HttpServletRequest request);
    RoomVo findById(String roomId);
    void enterChatRoom(String roomId, String userId);
    ChannelTopic getTopic(String roomId);
    List<RoomVo> findAllChatRoom(int page);
    String getRoomId(String destination);
    void setUserEnterInfo(String sessionId, String roomId);
    ChatRoom3 enterRoom(RoomEnterReqVo reqVo, HttpServletRequest request);
    List<String> getParticipant(int page, String roomId, HttpServletRequest request);
    RoomSearchVo searchRoom(String keyword, int page, HttpServletRequest request);
}
