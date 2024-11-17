package com.iiie.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

public class CaregiverDTO {

  private Long id;
  private String name;
  private String phone;
  private String hospital;
  private Long kakaoId;
  private Double mannerScore;
  private String patientName;

  @Getter
  @Setter
  public static class CreationCaregiver {
    @Schema(description = "Name of the caregiver", example = "간병인1")
    private String name;

    @Schema(description = "Phone number", example = "01012345678")
    private String phone;

    @Schema(description = "Hospital", example = "연세세브란스")
    private String hospital;
  }

  @Getter
  @Setter
  public static class InquiryCaregiver {
    private String name;
    private String phone;
    private String hospital;
    private String patientName;
  }

  @Getter
  @Setter
  public static class UpdateCaregiver {
    private String name;
    private String phone;
    private String hospital;
  }
}
