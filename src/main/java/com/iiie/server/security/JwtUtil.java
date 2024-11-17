package com.iiie.server.security;

import com.iiie.server.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {

  private final JwtConfig jwtConfig;

  public JwtUtil(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public Long getUserIdFromToken(String token) {
    log.info("AccessToken에서 ID 추출");
    Claims claims =
        Jwts.parserBuilder()
            .setSigningKey(jwtConfig.SECRET_KEY) // SecretKey 설정
            .build()
            .parseClaimsJws(token)
            .getBody();
    return Long.parseLong(claims.get("userId").toString());
  }
}
