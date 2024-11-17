package com.iiie.server.service;

import java.io.IOException;
import java.util.HashMap;

public interface KakaoService {

  HashMap<String, Object> getUserInfo(String accessToken) throws IOException;

  String getAccessTokenFromKakao(String clientId, String code) throws IOException;
}
