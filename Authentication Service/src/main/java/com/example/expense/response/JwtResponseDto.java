package com.example.expense.response;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
public class JwtResponseDto {
    private String accessToken;
    private String token;
}
