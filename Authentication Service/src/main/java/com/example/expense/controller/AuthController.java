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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


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
            String userId = userDetailsService.signupUser(userInfoDto);
            if(Objects.isNull(userId)){
                return new ResponseEntity<>("Already Exist", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(JwtResponseDto.builder().accessToken(jwtToken).token(refreshToken.getToken())
                    .userId(userId).build(),HttpStatus.OK);

        }catch (Exception ex){
            return new ResponseEntity<>("Error in user service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth/v1/ping")
    public ResponseEntity<Map<String, String>> ping() {
        System.out.println(">>> /ping endpoint hit");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(">>> Auth object: " + authentication);

        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println(">>> Authenticated user: " + authentication.getName());

            String userId = userDetailsService.findByUsername(authentication.getName());
            System.out.println(">>> userId from DB: " + userId);

            if (Objects.nonNull(userId)) {
                Map<String, String> response = new HashMap<>();
                response.put("userId", userId);
                return ResponseEntity.ok(response);
            }
        }

        Map<String, String> error = new HashMap<>();
        error.put("message", "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }


}
