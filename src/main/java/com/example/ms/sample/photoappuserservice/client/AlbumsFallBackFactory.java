package com.example.ms.sample.photoappuserservice.client;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class AlbumsFallBackFactory implements FallbackFactory<AlbumServiceFeignClient> {

    @Override
    public AlbumServiceFeignClient create(Throwable cause) {
        return new AlbumsClientFallBackImpl(cause);
    }
}
