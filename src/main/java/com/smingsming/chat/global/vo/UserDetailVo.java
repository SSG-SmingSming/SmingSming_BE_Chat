package com.smingsming.chat.global.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDetailVo {
    private String id;
    private String userEmail;
    private String nickName;
    private String userThumbnail;
}
