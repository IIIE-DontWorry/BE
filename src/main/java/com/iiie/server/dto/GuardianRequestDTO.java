package com.iiie.server.dto;

import com.iiie.server.domain.CareReport;
import com.iiie.server.domain.Guardian;
import lombok.Getter;
import lombok.Setter;

public class GuardianRequestDTO {

  private Long id;
  private String request;
  private Guardian guardian;
  private CareReport careReport;

  @Getter
  @Setter
  public static class GuardianRequest {
    private Long id;
    private String request;
    private Guardian guardian;
    private CareReport careReport;
  }
}
