package com.iiie.server.controller;

import com.iiie.server.config.JwtConfig;
import com.iiie.server.config.OauthConfig;
import com.iiie.server.dto.CaregiverDTO;
import com.iiie.server.dto.GuardianAndPatientDTO;
import com.iiie.server.dto.UserDTO.Response;
import com.iiie.server.service.KakaoService;
import com.iiie.server.service.UserService;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("")
public class UserController {

  private final OauthConfig oauthConfig;
  private final KakaoService kakaoService;
  private final UserService userService;

  public UserController(
      JwtConfig jwtConfig,
      OauthConfig oauthConfig,
      KakaoService kakaoService,
      UserService userService) {
    this.oauthConfig = oauthConfig;
    this.kakaoService = kakaoService;
    this.userService = userService;
  }
  // TODO : 로그인 및 회원가입 시 AccessToken외 **보호자, 간병인, 지인 구별 문자열도 같이 넘겨주기**
  @PostMapping("/guardians/login")
  @Operation(summary = "보호자 회원가입 OR 로그인", description = "(환자 정보와 함께)이미 존재하면 로그인. 처음이면 회원가입을 진행한다.")
  public SuccessResponse<Response> guardianLogin(
      @RequestParam("kakaoAccessToken") String kakaoAccessToken,
      @RequestBody GuardianAndPatientDTO.CreationRequest request)
      throws IOException {

    log.info("userLogin 진입...");
    log.info("카카오 토큰으로부터 유저 정보 파싱중...");
    HashMap<String, Object> userInfo = kakaoService.getUserInfo(kakaoAccessToken);

    log.info("보호자 회원가입 및 로그인 진행 중...");
    Response guardianTokens = userService.guardianValidation(userInfo, request);

    return new SuccessResponse<>("환자 딸린 보호자 생성 완료", guardianTokens);
  }

  @PostMapping("/caregivers/login")
  @Operation(
      summary = "간병인 회원가입 OR 로그인",
      description = "(보호자 인증 코드를 가지고)이미 존재하면 로그인. 처음이면 회원가입을 진행한다.")
  public SuccessResponse<?> caregiverLogin(
      @RequestParam("kakaoAccessToken") String kakaoAccessToken,
      @RequestBody CaregiverDTO.CreationCaregiver request)
      throws IOException {

    log.info("userLogin 진입...");
    log.info("카카오 토큰으로부터 유저 정보 파싱중...");
    HashMap<String, Object> userInfo = kakaoService.getUserInfo(kakaoAccessToken);

    log.info("간병인 회원가입 및 로그인 진행 중...");
    Response guardianTokens = userService.caregiverValidation(userInfo, request);

    return new SuccessResponse<>("보호자 인증 코드로 간병인 매칭(가입) 완료", guardianTokens);
  }

  @GetMapping("/callback")
  @Operation(
      summary = "카카오 토큰을 발급한다.",
      description = "카카오 인가 코드로 카카오 토큰을 발급합니다.(카카오 로그인 정보가 담긴 토큰을 가져옵니다.)")
  public SuccessResponse<?> callback(
      @RequestParam("code") String code, HttpServletResponse response) throws IOException {

    // === 카카오 인증 토큰 발급 from 인증코드 === //
    log.info("code : " + code);
    String kakaoAccessToken =
        kakaoService.getAccessTokenFromKakao(oauthConfig.CLIENT_ID, code); // kakao_access_token

    return new SuccessResponse<>("카카오 인증 토큰 발급 완료! 해당 토큰과 Role을 가지로 로그인을 진행하세요.", kakaoAccessToken);
  }
}
