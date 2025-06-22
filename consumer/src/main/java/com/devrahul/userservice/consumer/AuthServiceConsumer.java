package com.devrahul.userservice.consumer;

import com.devrahul.userservice.entities.UserInfo;
import com.devrahul.userservice.entities.UserInfoDto;
import com.devrahul.userservice.repository.UserRepository;
import com.devrahul.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceConsumer {

    @Autowired
    private UserService userService;


    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.name}")
    public void listen(UserInfoDto eventData){
        try {
            userService.createOrUpdateUser(eventData);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
