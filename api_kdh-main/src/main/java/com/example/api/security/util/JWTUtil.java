package com.example.api.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {
  private String secretKey = "1234567890abcdefghijklmnopqrstuvwxyz";
  private long expire = 60 * 24 * 30; // 만료 시간 (분)

  // JWT 생성 (이메일과 mid를 인자로 받음)
  public String generateToken(String email, Long mid) throws Exception {
    return Jwts.builder()
        .issuedAt(new Date())
        .expiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
        .claim("email", email) // 이메일을 클레임에 추가
        .claim("mid", mid) // 멤버 ID를 클레임에 추가
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .compact();
  }

  // JWT 검증 및 이메일, mid 추출
// JWT 검증 및 Claims 반환
  public Claims validateAndExtract(String tokenStr) throws Exception {
    log.info("JWT 검증 시작");
    Claims claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .build().parseClaimsJws(tokenStr).getBody();

    return claims; // Claims 반환
  }

}