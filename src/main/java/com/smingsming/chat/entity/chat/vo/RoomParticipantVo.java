package com.smingsming.chat.entity.chat.vo;

import com.smingsming.chat.global.vo.UserDetailVo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomParticipantVo {
    Long count;
    List<UserDetailVo> participantList;
}
