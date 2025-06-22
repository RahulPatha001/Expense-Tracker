package com.example.expense.service;

import com.example.expense.entities.RefreshToken;
import com.example.expense.entities.UserInfo;
import com.example.expense.repository.RefreshTokenRepository;
import com.example.expense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        UserInfo extractedUserInfo = userRepository.findByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(extractedUserInfo)
                .token(UUID.randomUUID().toString())
                .expireDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpireDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "This token is expired, please login again !");
        }
        return token;
    }

    public Optional<RefreshToken> findBytoken(String token){
        return refreshTokenRepository.findByToken(token);
    }

}
