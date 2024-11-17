package com.iiie.server.security;

import com.iiie.server.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  private final JwtConfig jwtConfig;

  public JwtTokenProvider(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  private final Long ACCESS_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60 * 24;

  public String createAccessToken(Long userId) {
    log.info("SECRET KEY FROM PROVIDER: {}", jwtConfig.SECRET_KEY);
    Date now = new Date();
    Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);

    byte[] bytes = String.valueOf(jwtConfig.SECRET_KEY).getBytes(StandardCharsets.UTF_8);
    System.out.println("Byte Length: " + bytes.length);

    return Jwts.builder()
        .signWith(SignatureAlgorithm.HS512, String.valueOf(jwtConfig.SECRET_KEY))
        .claim("userId", userId)
        .setIssuedAt(now)
        .setExpiration(validity)
        .compact();
  }
}
