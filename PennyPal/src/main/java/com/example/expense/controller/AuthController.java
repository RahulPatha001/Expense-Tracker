package com.example.expense.controller;

import com.example.expense.entities.RefreshToken;
import com.example.expense.model.UserInfoDto;
import com.example.expense.response.JwtResponseDto;
import com.example.expense.service.JwtService;
import com.example.expense.service.RefreshTokenService;
import com.example.expense.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/test")
    public String test(){
        System.out.println("herre");
        return "hello";
    }

    @PostMapping("auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto){
        System.out.println(userInfoDto);
        try {
            Boolean isUserSignedUp = userDetailsService.signupUser(userInfoDto);
            if(Boolean.FALSE.equals(isUserSignedUp)){
                return new ResponseEntity<>("User is already signed up !", HttpStatus.BAD_REQUEST);
                }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(JwtResponseDto.builder().accessToken(jwtToken).token(refreshToken.getToken())
                    .build(),HttpStatus.OK);

        }catch (Exception ex){
            return new ResponseEntity<>("Error in user service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
