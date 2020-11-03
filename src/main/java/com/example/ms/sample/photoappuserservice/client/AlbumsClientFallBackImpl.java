package com.example.ms.sample.photoappuserservice.client;

import com.example.ms.sample.photoappuserservice.model.AlbumResponseModel;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AlbumsClientFallBackImpl implements AlbumServiceFeignClient {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Throwable cause;

    public AlbumsClientFallBackImpl(Throwable cause){
        this.cause = cause;
    }

    @Override
    public List<AlbumResponseModel> getAlbums(String userId) {
        if(cause instanceof FeignException && ((FeignException)cause).status() == 400){
            logger.error("404 error occurred while calling with user Id : "+userId+
                    " with error message : "+cause.getLocalizedMessage());
        }else{
            logger.error("Other error took place : "+cause.getLocalizedMessage());
        }
        return new ArrayList<>();
    }
}
