package com.devrahul.userservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDto {
    Long Id;
    @Id
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone_no")
    private Long phoneNo;

    private String email;
    @JsonProperty("profile_pic")
    private String profilePic;


    public UserInfo transformToUserInfo(){
        return UserInfo.builder().firstName(firstName).lastName(lastName)
                .email(email).phoneNo(phoneNo).userId(userId).profilePic(profilePic)
                .build();
    }
}
