package com.smingsming.chat.entity.chat.service;

import com.smingsming.chat.entity.chat.entity.ChatRoom;
import com.smingsming.chat.entity.chat.entity.Participant;
import com.smingsming.chat.entity.chat.repository.IChatRoomRepository;
import com.smingsming.chat.entity.chat.repository.IParticipantRepository;
import com.smingsming.chat.entity.chat.vo.*;
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
    private static final String ENTER_INFO = "ENTER_INFO";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, String> opsEnterInfo;
    private Map<String, ChannelTopic> topics;
    private final JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    private void init() {
        opsEnterInfo = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    //  채팅방 정보 조회
    @Override
    public RoomVo findById(String roomId) {

        ChatRoom room = iChatRoomRepository.findById(roomId).get();

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

        RoomVo roomVo = new ModelMapper().map(room, RoomVo.class);
        roomVo.setParticipant(iParticipantRepository.countParticipantByRoomId(roomId));
        roomVo.setUserName(user.getNickName());
        roomVo.setUserThumbnail(user.getUserThumbnail());
        roomVo.setPlaylistName(playlistVo.getTitle());
        roomVo.setPlaylistThumbnail(playlistVo.getPlaylistThumbnail());

        return roomVo;
    }

    // 채팅방 생성
    @Override
    public ChatRoom createChatRoom(RoomAddReqVo roomAddReqVo, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUuid(jwtTokenProvider.resolveToken(request));

        ChatRoom chatRoom = ChatRoom.create(roomAddReqVo.getName(), roomAddReqVo.getPassword(),
                roomAddReqVo.isLock(), userId, roomAddReqVo.getPlaylistId());

        iChatRoomRepository.save(chatRoom);

        return chatRoom;
    }

    // 채팅방 입장 (stomp 연결)
    @Override
    public void enterChatRoom(String roomId, String userId) {
        ChannelTopic topic = topics.get(roomId);

        Optional<ChatRoom> room = iChatRoomRepository.findById(roomId);

        if(topic == null)
            topic = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);

        iParticipantRepository.save(Participant.builder()
                .userId(userId)
                .chatRoom(room.get())
                .build());
    }

    // 채팅방 퇴장
    @Override
    @Transactional
    public void quitRoom(String roomId, String userId) {
        iParticipantRepository.deleteByUserIdAndChatRoomId(userId, roomId);

        if (iParticipantRepository.countByChatRoomId(roomId) == 0) {
            iChatRoomRepository.deleteById(roomId);
            iParticipantRepository.deleteByChatRoomId(roomId);
        }
    }

    // 채팅방 입장 (웹)
    @Override
    public ChatRoom enterRoom(RoomEnterReqVo reqVo, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUuid(jwtTokenProvider.resolveToken(request));

        Optional<ChatRoom> room = iChatRoomRepository.findById(reqVo.getRoomId());

        if(!room.isPresent())
            return null;

        if (room.get().isLock()) {
            if (! room.get().getPassword().equals(reqVo.getPassword()))
                return null;
        }

        return room.get();
    }

    // 채팅방 목록 조회
    @Override
    public List<RoomVo> findAllChatRoom(int page) {
        Pageable pr = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        
        Page<ChatRoom> roomList = iChatRoomRepository.findAll(pr);

        List<RoomVo> returnList = new ArrayList<>();

        roomList.forEach(v -> {

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
                    .participant(iParticipantRepository.countParticipantByRoomId(v.getId()))
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

    // Redis Topic 조회
    @Override
    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

    // 채팅방 참가자 목록 조회
    @Override
    public List<String> getParticipant(int page, String roomId, HttpServletRequest request) {
        List<ParticipantVo> returnVo = new ArrayList<>();

        Pageable pr = PageRequest.of(page - 1, 10, Sort.by("id").descending());

        List<String> participantList = iParticipantRepository.getParticipant(roomId);

        return participantList;
    }

    // 채팅방 검색
    @Override
    public RoomSearchVo searchRoom(String keyword, int page, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUuid(jwtTokenProvider.resolveToken(request));

        Pageable pr = PageRequest.of(page -1, 10, Sort.by("id").descending());

        Page<ChatRoom> roomPage = iChatRoomRepository.findAllByNameContains(keyword, pr);

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

            RoomVo roomVo = mapper.map(v, RoomVo.class);
            roomVo.setParticipant(iParticipantRepository.countParticipantByRoomId(v.getId()));
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
