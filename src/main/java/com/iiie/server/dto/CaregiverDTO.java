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

  @Getter
  @Setter
  public static class CreationCaregiver {
    @Schema(description = "Name of the caregiver", example = "간병인1")
    private String name;

    @Schema(description = "Phone number", example = "01012345678")
    private String phone;

    @Schema(description = "Hospital", example = "연세세브란스")
    private String hospital;

    @Schema(description = "Career histories")
    List<String> careerHistories; // 경력사항

    @Schema(description = "Guardian unique code", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID guardianUniqueCode;
  }
}
