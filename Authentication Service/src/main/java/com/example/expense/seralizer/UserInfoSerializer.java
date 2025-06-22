package com.example.expense.seralizer;

import com.example.expense.eventProducer.UserInfoEvent;
import com.example.expense.model.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserInfoSerializer implements Serializer<UserInfoEvent> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, UserInfoEvent userInfoEvent) {
        byte[] returnval = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            returnval = objectMapper.writeValueAsString(userInfoEvent).getBytes();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return returnval;
    }

    @Override
    public byte[] serialize(String topic, Headers headers, UserInfoEvent data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
