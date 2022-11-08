package com.smingsming.chat.global.client;

import com.smingsming.chat.global.vo.UserDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-server")
public interface UserServiceClient {

    @GetMapping("/user/get/{id}")
    UserDetailVo getUser(@PathVariable(value = "id") String id);
}
