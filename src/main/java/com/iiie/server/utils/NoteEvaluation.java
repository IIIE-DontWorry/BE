package com.iiie.server.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NoteEvaluation {
    @Value("${openai.api-url}")
    private String apiUrl;

    @Value("${openai.api-key}")
    private String apiKey;

    private final String prompt = "다음은 사용자의 대화 내용을 분석하는 작업이다. 대화 내용을 기반으로 대화를 평가하여 다음 기준에 따라 정확히 0, 1, 2 중 하나의 값을 반환해줘. 응답은 반드시 숫자만 반환해야 하며, 다른 설명이나 추가 텍스트는 절대 포함하지마. - 0: 부정적인 대화. 대화 내용에 감정적으로 부정적인 요소(예: 화, 짜증, 불만, 슬픔)가 포함되어 있거나 상대방에게 불쾌감을 줄 수 있는 표현을 포함하는 경우. - 1: 평범한 대화. 대화가 중립적이거나 감정 표현이 거의 없으며, 긍정적 또는 부정적 요소가 뚜렷하지 않은 경우. - 2: 긍정적인 대화. 대화 내용이 감정적으로 긍정적인 요소(예: 기쁨, 칭찬, 격려, 친절)를 포함하거나 상대방에게 호감을 줄 수 있는 표현이 포함된 경우. 위 기준을 철저히 준수하여 대화를 분석하고, 0, 1, 2 중 하나의 값을 반환해줘. <평가 예시> \"어머니가 요즘 많이 편찮으시다 하시더라고요\"의 경우 부정적인 느낌이 있긴 하지만 존댓말을 사용해서 상대방을 배려해주고 있으니 0점으로 평가해줘, \"내일은 가족들과 함께 찾아갈 예정입니다.\"의 경우 단순히 사실을 언급 했을 뿐이고 어떤 감정도 포함되어 있지 않으니 0점으로 평가해줘 </평가 예시>";

    public String evaluateContent(String content) {
        // 요청 본문 생성
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4");
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

