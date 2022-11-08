package com.smingsming.chat.global.client;

import com.smingsming.chat.global.vo.PlaylistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "song-server")
public interface SongServiceClient {

    @GetMapping("/playlist/get/id/{playlistId}")
    PlaylistVo getPlaylist(@PathVariable(name = "playlistId") Long playlistId);
}
