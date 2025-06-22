package com.devrahul.userservice.service;

import com.devrahul.userservice.entities.UserInfo;
import com.devrahul.userservice.entities.UserInfoDto;
import com.devrahul.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserInfoDto createOrUpdateUser(UserInfoDto userInfoDto){
        UnaryOperator<UserInfo> updatingUser = user -> {
             return userRepository.save(userInfoDto.transformToUserInfo());
        };

        Supplier<UserInfo> createUser = () -> {
            return userRepository.save(userInfoDto.transformToUserInfo());
        };

        UserInfo userInfo = userRepository.findByUserId(userInfoDto.getUserId())
                .map(updatingUser)
                .orElseGet(createUser);
        return UserInfoDto.builder()
                .userId(userInfo.getUserId())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .phoneNo(userInfoDto.getPhoneNo())
                .email(userInfo.getEmail())
                .profilePic(userInfo.getProfilePic())
                .build();
    }

    public UserInfoDto getUser(UserInfoDto userInfoDto) throws Exception{
        Optional<UserInfo> userInfoDtoOpt = userRepository.findByUserId(userInfoDto.getUserId());
        if(userInfoDtoOpt.isEmpty()){
            throw new Exception("User not found");
        }
        UserInfo userInfo = userInfoDtoOpt.get();
        return UserInfoDto.builder()
                .userId(userInfo.getUserId())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .phoneNo(userInfoDto.getPhoneNo())
                .email(userInfo.getEmail())
                .profilePic(userInfo.getProfilePic())
                .build();
    }
}
