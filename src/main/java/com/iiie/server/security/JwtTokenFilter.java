package com.iiie.server.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final int TOKEN_START_INDEX = 7;

  public JwtTokenFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String requestURI = request.getRequestURI();
    if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
      filterChain.doFilter(request, response);
      return; // 필터 체인을 중단하지 않도록 바로 반환
    }

    log.info("JwtTOkenFilter진입");

    final String authorizationHeader = request.getHeader("Authorization");
    log.info("AuthorizationHeader : {}", authorizationHeader);

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      String token = authorizationHeader.substring(TOKEN_START_INDEX);
      log.info("token : {}", token);
      try {
        // TODO: JWT 토큰 검증(예외 처리 만들어야함)

        // JWT토큰에서 사용자 정보 추출
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userId, null, null);

        // SecurituContextHolder에 인증 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
      } catch (Exception e) {

        e.printStackTrace();
        // JWT 토큰 검증 실패 시, 인증 객체를 null로 설정
        SecurityContextHolder.clearContext();
        filterChain.doFilter(request, response);
      }
    } else {
      log.info("JwtTokenFilter의 else 구문으로 분기함");
      filterChain.doFilter(request, response);
    }
  }
}
