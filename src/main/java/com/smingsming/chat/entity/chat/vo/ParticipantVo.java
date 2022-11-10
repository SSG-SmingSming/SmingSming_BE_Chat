package com.smingsming.chat.entity.chat.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantVo {
    Long id;
    String userId;
    String roomId;
}
