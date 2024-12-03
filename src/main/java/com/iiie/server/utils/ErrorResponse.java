package com.iiie.server.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "에러 응답 구조")
public class ErrorResponse {

  @Schema(description = "응답 상태", example = "error")
  private String status;

  @Schema(description = "응답 메시지", example = "An error occurred")
  private String message;

  @Schema(description = "에러 정보 목록")
  private List<ErrorDetail> errors;

  public ErrorResponse(String message, List<ErrorDetail> errors) {
    this.status = "error";
    this.message = message;
    this.errors = errors;
  }
}
