package com.iiie.server.dto;

import lombok.Builder;
import lombok.Data;

public class UserDTO {

  @Data
  @Builder
  public static class Response {
    private String accessToken;
  }
}
