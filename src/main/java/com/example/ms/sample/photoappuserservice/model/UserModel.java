package com.example.ms.sample.photoappuserservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserModel {

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, message = "First name must not be less than two characters")
    private String firstName;
    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, message = "Last name must not be less than two characters")
    private String lastName;
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, max = 16 , message = "Password is invalid")
    private String password;
    @NotNull(message = "Email cannot be null")
    @Email
    private String email;

}
