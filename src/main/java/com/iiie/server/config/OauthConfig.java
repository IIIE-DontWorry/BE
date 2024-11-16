package com.iiie.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OauthConfig {

    @Value("${kakao.client_id}")
    public String CLIENT_ID;
}
