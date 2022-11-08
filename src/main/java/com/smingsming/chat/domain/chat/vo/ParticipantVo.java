package com.smingsming.chat.domain.chat.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantVo {
    Long id;
    String userId;
    String roomId;
}
