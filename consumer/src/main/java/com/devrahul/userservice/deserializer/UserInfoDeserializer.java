package com.devrahul.userservice.deserializer;

import com.devrahul.userservice.entities.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.util.Map;

public class UserInfoDeserializer implements Deserializer<UserInfoDto> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public UserInfoDto deserialize(String s, byte[] bytes) {
        UserInfoDto user = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            user = objectMapper.readValue(bytes, UserInfoDto.class);
            System.out.println(user);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return user;
    }

    @Override
    public UserInfoDto deserialize(String topic, Headers headers, byte[] data) {
        return Deserializer.super.deserialize(topic, headers, data);
    }



    @Override
    public void close() {
        Deserializer.super.close();
    }
}
