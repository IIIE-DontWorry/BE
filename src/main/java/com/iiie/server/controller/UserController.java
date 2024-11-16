package com.iiie.server.controller;

import com.iiie.server.config.JwtConfig;
import com.iiie.server.config.OauthConfig;
import com.iiie.server.service.KakaoService;
import com.iiie.server.utils.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("")
public class UserController {

    private final OauthConfig oauthConfig;
    private final KakaoService kakaoService;

    public UserController(JwtConfig jwtConfig, OauthConfig oauthConfig, KakaoService kakaoService) {
        this.oauthConfig = oauthConfig;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/callback")
    public SuccessResponse<?> callback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {

        log.info("code : " + code);
        String kakao_accessToken = kakaoService.getAccessTokenFromKakao(oauthConfig.CLIENT_ID, code);//kakao_access_token

        return new SuccessResponse<>("카카오 인증 토큰 발급 완료", kakao_accessToken);
    }
}
