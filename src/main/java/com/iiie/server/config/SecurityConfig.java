package com.iiie.server.config;

import com.iiie.server.security.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtTokenFilter jwtTokenFilter;

  public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
    this.jwtTokenFilter = jwtTokenFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/api/swagger/**", "/api/swagger-ui/**", "/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers("/guardians/login/**", "/caregivers/login/**")
                    .permitAll()
                    .requestMatchers("/kakao")
                    .permitAll()
                    .requestMatchers("/callback")
                    .permitAll()
                    .requestMatchers("/care-givers", "/guardian")
                    .permitAll() // 카카오 로그인 아닌 회원가입 개발용
                    .anyRequest()
                    .permitAll()) //TODO: 실제 main 환경에서는 authenticated로 변경할것
        .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
