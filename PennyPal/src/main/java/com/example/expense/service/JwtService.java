package com.example.expense.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    public static final String SECRET = "87d25bc27a8e6729bd4acac91f784bf36e0baede347cdb8af10f196a159e9a2f46c2c724817ca276ee927cafa3719301729a79751e98d173de60339184d46e7f78ef2c9af356a2b7b58fbd2c9da327002ba85df5729a7c5498bd3ca594fbaeaaec3d333e0f678d1bc166284a4c81e3736a8640df026f0d2975faf6ba3a25cd941eedfb42d03b4cc7a053ecbbd82499ece7350e490bef40b2317f26a813c4be2d5b2bd83d09c010a03078883a716c56ec1dcfdd81af2990bbd141b6c77c18f08f59173278a32c481890ee5f4926eab0e0a576a116e1bb86ff208265c1f3b1631e21adb59dbf362eed74924554441609389df0303d101fe2219b5ca6ad09d54a16";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims =  extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Date extractExpiration(String token){

        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
    }

    public String createToken(Map<String, Object> claims, String username){
        return Jwts.builder().claims().add(claims).subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 1000*60*30))
                .and().signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public String GenerateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
