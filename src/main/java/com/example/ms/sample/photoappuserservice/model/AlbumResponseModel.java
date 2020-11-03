package com.example.ms.sample.photoappuserservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AlbumResponseModel {

    private long id;
    private String albumId;
    private String userId;
    private String name;
    private String description;

}
