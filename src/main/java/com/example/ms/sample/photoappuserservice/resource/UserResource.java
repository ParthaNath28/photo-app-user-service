package com.example.ms.sample.photoappuserservice.resource;

import com.example.ms.sample.photoappuserservice.dto.UserDTO;
import com.example.ms.sample.photoappuserservice.model.UserModel;
import com.example.ms.sample.photoappuserservice.model.UserResponse;
import com.example.ms.sample.photoappuserservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserModel userDetail){
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        final UserDTO userDTO = modelMapper.map(userDetail, UserDTO.class);
        final UserDTO savedUserDTO = userService.createUser(userDTO);
        final UserResponse userResponse = modelMapper.map(savedUserDTO, UserResponse.class);
        /*URI location =
                ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(
                        savedUserDTO.getUserId())
                .toUri();*/

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId){
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        final UserDTO userDTO = userService.findByUserId(userId);
        final UserResponse userResponse = modelMapper.map(userDTO, UserResponse.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponse);
    }



}
