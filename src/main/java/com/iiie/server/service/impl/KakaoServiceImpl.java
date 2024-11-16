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
    public HashMap<String, Object> getUserInfo(String accessToken) {
        return null;
    }

    @Override
    public String getAccessTokenFromKakao(String clientId, String code) throws IOException {

        //-----Kakao POST 요청-----//
        String REQUEST_URL =
                "https://kauth.kakao.com/oauth/token?grant_type=authorization_code"+
                        "&client_id=" + clientId +
                        "&code=" + code;

        URL url = new URL(REQUEST_URL);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
        });

        log.info("Response Body : " + result);

        String accessToken = (String) jsonMap.get("access_token");

        return accessToken;
        }
}
