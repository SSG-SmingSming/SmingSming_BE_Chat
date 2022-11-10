package com.smingsming.chat.entity.chat.service;

import com.smingsming.chat.entity.chat.entity.ChatRoom;
import com.smingsming.chat.entity.chat.vo.RoomAddReqVo;
import com.smingsming.chat.entity.chat.vo.RoomEnterReqVo;
import com.smingsming.chat.entity.chat.vo.RoomSearchVo;
import com.smingsming.chat.entity.chat.vo.RoomVo;
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
