package com.example.ms.sample.photoappuserservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 60, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false)
    private String encryptedPassword;

}
