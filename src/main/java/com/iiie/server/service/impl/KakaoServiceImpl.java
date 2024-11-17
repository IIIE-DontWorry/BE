package com.iiie.server.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iiie.server.service.KakaoService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KakaoServiceImpl implements KakaoService {

  @Override
  public HashMap<String, Object> getUserInfo(String accessToken) throws IOException {

    HashMap<String, Object> userInfo = new HashMap<>();

    // === Kakao GET 요청 === //
    final String REQUEST_URL = "https://kapi.kakao.com/v2/user/me";
    URL url = new URL(REQUEST_URL);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Authorization", "Bearer " + accessToken);

    int responseCode = conn.getResponseCode();
    log.info("responseCode : " + responseCode);

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    String line = "";
    String result = "";

    while ((line = br.readLine()) != null) {
      result += line;
    }

    log.info("response body : " + result);
    log.info("result type" + result.getClass().getName()); // java.lang.String

    // jackson objectmapper 객체 생성
    ObjectMapper objectMapper = new ObjectMapper();
    // JSON String -> Map
    Map<String, Object> jsonMap =
        objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});

    System.out.println(jsonMap.get("properties"));

    Long id = (Long) jsonMap.get("id");
    userInfo.put("id", id);

    return userInfo;
  }

  @Override
  public String getAccessTokenFromKakao(String clientId, String code) throws IOException {

    // -----Kakao POST 요청-----//
    String REQUEST_URL =
        "https://kauth.kakao.com/oauth/token?grant_type=authorization_code"
            + "&client_id="
            + clientId
            + "&code="
            + code;

    URL url = new URL(REQUEST_URL);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    String line = "";
    String result = "";

    while ((line = br.readLine()) != null) {
      result += line;
    }

    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> jsonMap =
        objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});

    log.info("Response Body : " + result);

    String accessToken = (String) jsonMap.get("access_token");

    return accessToken;
  }
}
