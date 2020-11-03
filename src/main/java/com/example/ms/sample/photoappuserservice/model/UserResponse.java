package com.example.ms.sample.photoappuserservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private List<AlbumResponseModel> albumResponseModelList;
}
