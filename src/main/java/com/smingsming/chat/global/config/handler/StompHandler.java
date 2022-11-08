package com.smingsming.chat.global.config.handler;

import com.smingsming.chat.domain.chat.service.IChatRoomService;
import com.smingsming.chat.global.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final IChatRoomService iChatRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String roomId = iChatRoomService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            iChatRoomService.setUserEnterInfo(sessionId, roomId);
        }

        return ChannelInterceptor.super.preSend(message, channel);

    }
}
