package com.iiie.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws.credentials")
public class AwsCredentials {

  private String accessKey;
  private String secretKey;

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }
}
