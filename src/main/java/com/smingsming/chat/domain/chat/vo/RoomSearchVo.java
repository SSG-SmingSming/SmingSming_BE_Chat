package com.smingsming.chat.domain.chat.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomSearchVo {
    Long count;
    List<RoomVo> result;
}
