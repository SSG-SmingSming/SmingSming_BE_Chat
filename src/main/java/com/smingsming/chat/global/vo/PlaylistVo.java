package com.smingsming.chat.global.vo;

import lombok.*;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistVo {

    private Long id;
    private String title;
    private Timestamp createDate;
    private  Timestamp updateDate;
    private String playlistThumbnail;
    private Long userId;
}

