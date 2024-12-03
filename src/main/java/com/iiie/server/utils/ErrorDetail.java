package com.iiie.server.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetail {

  private String code;

  private String message;

  private String details;

  public ErrorDetail(String code, String message) {
    this.code = code;
    this.message = message;
    this.details = null; // 기본값은 null
  }

  public ErrorDetail(String code, String message, String details) {
    this.code = code;
    this.message = message;
    this.details = details;
  }
}
