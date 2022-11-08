package com.smingsming.chat.domain.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomEnterReqVo {
    private String roomId;
    private String password;
}
