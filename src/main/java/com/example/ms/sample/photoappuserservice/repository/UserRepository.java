package com.example.ms.sample.photoappuserservice.repository;

import com.example.ms.sample.photoappuserservice.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByUserId(String userId);

    UserEntity findByEmail(String email);

}
