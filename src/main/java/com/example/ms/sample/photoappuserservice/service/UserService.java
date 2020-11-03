package com.example.ms.sample.photoappuserservice.service;

import com.example.ms.sample.photoappuserservice.client.AlbumServiceFeignClient;
import com.example.ms.sample.photoappuserservice.dto.UserDTO;
import com.example.ms.sample.photoappuserservice.entity.UserEntity;
import com.example.ms.sample.photoappuserservice.model.AlbumResponseModel;
import com.example.ms.sample.photoappuserservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AlbumServiceFeignClient albumServiceFeignClient;


    public UserDTO createUser(final UserDTO userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        final UserEntity userEntity = getUserByModelMapper(userDetails);
        final UserDTO userDTO = getUserDTOByModelMapper(userRepository.save(userEntity));
        return userDTO;
    }

    private UserEntity getUserByModelMapper(UserDTO userDetails) {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(userDetails, UserEntity.class);
    }

    private UserDTO getUserDTOByModelMapper(UserEntity userEntity) {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(userEntity, UserDTO.class);
    }

    public UserDTO findByUserId(String userId) {
        List<AlbumResponseModel> albumResponseModelList = albumServiceFeignClient.getAlbums(userId);
        final UserDTO userDTO =  getUserDTOByModelMapper(userRepository.findByUserId(userId));
        userDTO.setAlbumResponseModelList(albumResponseModelList);
        return userDTO;
    }


    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByEmail(userEmail);
        if(userEntity == null){
            throw new UsernameNotFoundException(userEmail);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }

    public UserDTO findUserDetailsByEmail(String userEmail) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByEmail(userEmail);
        if(userEntity == null){
            throw new UsernameNotFoundException(userEmail);
        }
        return getUserDTOByModelMapper(userEntity);
    }
}
