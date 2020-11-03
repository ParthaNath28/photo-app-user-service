package com.example.ms.sample.photoappuserservice.client;

import com.example.ms.sample.photoappuserservice.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="albums-ws", fallbackFactory = AlbumsFallBackFactory.class)
public interface AlbumServiceFeignClient {

    @GetMapping("/users/{id}/albums")
    public List<AlbumResponseModel> getAlbums(@PathVariable String id);

}
