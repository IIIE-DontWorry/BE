package com.iiie.server.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class NoteEvaluation {
    @Value("${openai.api-url}")
    private String apiUrl;

    @Value("${openai.api-key}")
    private String apiKey;

    private final String prompt = "해당 사용자의 대화 내용을 분석해서 부정적인 대화일 경우 0, 평범한 대화일 경우 1, 긍정적인 대화일 경우 2를 반환해줘. 응답을 프로그램에서 사용할 거라 다른 말은 절대로 덧붙이지 말고 정확히 0 또는 1 또는 2 값만 반환해야 해. ";

    public String evaluateContent(String content) {
        // 요청 본문 생성
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("temperature", 0.0);
        requestBody.put("max_tokens", 32);
        requestBody.put("messages", new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", prompt))
                .put(new JSONObject().put("role", "user").put("content", content))
        );

        // HTTP 요청 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        // 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        // 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject responseBody = new JSONObject(response.getBody());
            return responseBody
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        } else {
            throw new RuntimeException("GPT API 호출 실패: " + response.getStatusCodeValue());
        }
    }
}
