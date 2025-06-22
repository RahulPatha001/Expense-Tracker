package com.example.expense.controller;

import com.example.expense.entities.RefreshToken;
import com.example.expense.request.AuthRequestDto;
import com.example.expense.request.RefrestTokenRequestDto;
import com.example.expense.response.JwtResponseDto;
import com.example.expense.service.JwtService;
import com.example.expense.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("auth/v1/login")
    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDto.getUsername());
            return new ResponseEntity<>(JwtResponseDto.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDto.getUsername()))
                    .token(refreshToken.getToken()).build(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Exception in getting token",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("auth/v1/refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefrestTokenRequestDto refrestTokenRequestDto){
        return  refreshTokenService.findBytoken(refrestTokenRequestDto.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JwtResponseDto.builder().accessToken(accessToken).token(refrestTokenRequestDto.getToken())
                            .build();
                }).orElseThrow(()-> new RuntimeException("Token not in Data Base"));
    }
}
