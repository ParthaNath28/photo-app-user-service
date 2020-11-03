package com.example.ms.sample.photoappuserservice.dto;


import com.example.ms.sample.photoappuserservice.model.AlbumResponseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDTO implements Serializable {

    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String userId;
    private String encryptedPassword;
    private List<AlbumResponseModel> albumResponseModelList;
}
