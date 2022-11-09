package com.smingsming.chat.domain.chat.service;

import com.smingsming.chat.domain.chat.entity.ChatRoom;
//import com.example.chat.domain.chat.entity.Managerqq;
//import com.example.chat.domain.chat.repository.IChatRoomRepository;
//import com.example.chat.domain.chat.repository.IManagerRepository;
import com.smingsming.chat.domain.chat.entity.ChatRoom3;
import com.smingsming.chat.domain.chat.entity.Participant;
import com.smingsming.chat.domain.chat.repository.IChatRoomRepository;
import com.smingsming.chat.domain.chat.repository.IParticipantRepository;
import com.smingsming.chat.domain.chat.vo.*;
import com.smingsming.chat.global.client.SongServiceClient;
import com.smingsming.chat.global.client.UserServiceClient;
import com.smingsming.chat.global.common.jwt.JwtTokenProvider;
import com.smingsming.chat.global.pubsub.RedisSubscriber;
import com.smingsming.chat.global.vo.PlaylistVo;
import com.smingsming.chat.global.vo.UserDetailVo;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomServiceImpl implements IChatRoomService{

    private final RedisMessageListenerContainer redisMessageListener;
    private final IChatRoomRepository iChatRoomRepository;
    private final IParticipantRepository iParticipantRepository;
    private final RedisSubscriber redisSubscriber;
    private final UserServiceClient userServiceClient;
    private final SongServiceClient songServiceClient;
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private static final String ENTER_INFO = "ENTER_INFO";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    private HashOperations<String, String, String> opsEnterInfo;
    private Map<String, ChannelTopic> topics;
    private final JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        opsEnterInfo = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    @Override
    public RoomVo findById(String roomId) {

        ChatRoom3 room = iChatRoomRepository.findById(roomId).get();

        UserDetailVo user = new UserDetailVo();

        PlaylistVo playlistVo = new PlaylistVo();
        try {
            user = userServiceClient.getUser(room.getUserId());
        } catch (FeignException ex) {
            log.info(ex.getMessage());
        }

        try {
            playlistVo = songServiceClient.getPlaylist(room.getPlaylistId());
        } catch (FeignException ex) {
            log.info(ex.getMessage());
        }

//        List<String> enterInfo = opsEnterInfo.values(room.getId());

        int count = iParticipantRepository.countByChatRoomId(roomId);

        RoomVo roomVo = new ModelMapper().map(room, RoomVo.class);
        roomVo.setParticipant(count);
        roomVo.setUserName(user.getNickName());
        roomVo.setUserThumbnail(user.getUserThumbnail());
        roomVo.setPlaylistName(playlistVo.getTitle());
        roomVo.setPlaylistThumbnail(playlistVo.getPlaylistThumbnail());

        return roomVo;
    }

    @Override
    public ChatRoom createChatRoom(RoomAddReqVo roomAddReqVo, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUuid(jwtTokenProvider.resolveToken(request));

        ChatRoom chatRoom = ChatRoom.create(roomAddReqVo.getName(), roomAddReqVo.getPassword(),
                roomAddReqVo.isLock(), userId, roomAddReqVo.getPlaylistId());

        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getId(), chatRoom);

        iChatRoomRepository.save(new ModelMapper().map(chatRoom, ChatRoom3.class));

        return chatRoom;
    }

    @Override
    public void enterChatRoom(String roomId, String userId) {
        ChannelTopic topic = topics.get(roomId);

        Optional<ChatRoom3> room = iChatRoomRepository.findById(roomId);

        if(topic == null)
            topic = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);

        iParticipantRepository.save(Participant.builder()
                .userId(userId)
                .chatRoom(room.get())
                .build());
    }


    @Override
    @Transactional
    public void quitRoom(String roomId, String userId) {
        iParticipantRepository.deleteByUserIdAndChatRoomId(userId, roomId);

        if (iParticipantRepository.countByChatRoomId(roomId) == 0) {
            iChatRoomRepository.deleteById(roomId);
            iParticipantRepository.deleteByChatRoomId(roomId);
        }
    }

    @Override
    public ChatRoom3 enterRoom(RoomEnterReqVo reqVo, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUuid(jwtTokenProvider.resolveToken(request));

        Optional<ChatRoom3> room = iChatRoomRepository.findById(reqVo.getRoomId());

        if(!room.isPresent())
            return null;

        if (room.get().isLock()) {
            if (! room.get().getPassword().equals(reqVo.getPassword()))
                return null;
        }

        if (iParticipantRepository.existsByChatRoomIdAndUserId(reqVo.getRoomId(), userId))
            return null;

        iParticipantRepository.save(Participant.builder()
                .userId(userId)
                .chatRoom(room.get())
                .build());

        return room.get();
    }


    @Override
    public List<RoomVo> findAllChatRoom(int page) {
        Pageable pr = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        
        Page<ChatRoom3> roomList = iChatRoomRepository.findAll(pr);

        List<RoomVo> returnList = new ArrayList<>();

        roomList.forEach(v -> {

            System.out.println(v.getId());
            System.out.println(iParticipantRepository.countParticipantByRoomId(v.getId()));
            UserDetailVo user = new UserDetailVo();
            PlaylistVo playlistVo = new PlaylistVo();
            try {
                user = userServiceClient.getUser(v.getUserId());
            } catch (FeignException ex) {
                log.info(ex.getMessage());
            }

            try {
                playlistVo = songServiceClient.getPlaylist(v.getPlaylistId());
            } catch (FeignException ex) {
                log.info(ex.getMessage());
            }

            RoomVo roomVo = RoomVo.builder()
                    .roomId(v.getId())
                    .roomName(v.getName())
                    .participant(iParticipantRepository.countByChatRoomId(v.getId()))
                    .isLock(v.isLock())
                    .userId(v.getUserId())
                    .userName(user.getNickName())
                    .userThumbnail(user.getUserThumbnail())
                    .playlistId(v.getPlaylistId())
                    .playlistName(playlistVo.getTitle())
                    .playlistThumbnail(playlistVo.getPlaylistThumbnail())
                    .build();

            returnList.add(roomVo);
        });


        return returnList;
    }

    @Override
    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

    @Override
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            return "";
        }
    }

    @Override
    public void setUserEnterInfo(String sessionId, String roomId) {
        opsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

//    @Override
//    public String getUserEnterRoomId(String sessionId) {
//        return opsEnterInfo.get(ENTER_INFO, sessionId);
//    }
//
//    @Override
//    public void remoteUserEnterInfo(String sessionId) {
//        opsEnterInfo.delete(ENTER_INFO, sessionId);
//    }



    @Override
    public List<String> getParticipant(int page, String roomId, HttpServletRequest request) {
        List<ParticipantVo> returnVo = new ArrayList<>();

        Pageable pr = PageRequest.of(page - 1, 10, Sort.by("id").descending());

//        List<Participant> participantList = iParticipantRepository.findAllByChatRoomId(pr, roomId);

        List<String> participantList = iParticipantRepository.getParticipant(roomId);



        return participantList;
    }

    @Override
    public RoomSearchVo searchRoom(String keyword, int page, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUuid(jwtTokenProvider.resolveToken(request));

        Pageable pr = PageRequest.of(page -1, 10, Sort.by("id").descending());

        Page<ChatRoom3> roomPage = iChatRoomRepository.findAllByNameContains(keyword, pr);

        List<RoomVo> returnList = new ArrayList<>();

        ModelMapper mapper = new ModelMapper();

        roomPage.forEach(v -> {
            UserDetailVo user = new UserDetailVo();
            PlaylistVo playlistVo = new PlaylistVo();
            try {
                user = userServiceClient.getUser(v.getUserId());
            } catch (FeignException ex) {
                log.info(ex.getMessage());

            }

            try {
                playlistVo = songServiceClient.getPlaylist(v.getPlaylistId());
            } catch (FeignException ex) {
                log.info(ex.getMessage());
            }

            List<String> enterInfo = opsEnterInfo.values(v.getId());

            RoomVo roomVo = mapper.map(v, RoomVo.class);
            roomVo.setParticipant(enterInfo.size());
            roomVo.setUserName(user.getNickName());
            roomVo.setUserThumbnail(user.getUserThumbnail());
            roomVo.setPlaylistName(playlistVo.getTitle());
            roomVo.setPlaylistThumbnail(playlistVo.getPlaylistThumbnail());
            returnList.add(roomVo);
        });

        return RoomSearchVo.builder()
                .count(roomPage.getTotalElements())
                .result(returnList)
                .build();
    }
}
