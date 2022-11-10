package com.smingsming.chat.entity.chat.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomSearchVo {
    Long count;
    List<RoomVo> result;
}
