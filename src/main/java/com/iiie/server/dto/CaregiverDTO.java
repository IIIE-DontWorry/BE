package com.iiie.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
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

    @Schema(description = "Career histories")
    private List<String> careerHistories; // 경력사항

    @Schema(description = "Guardian unique code", example = "(보호자 유니크 코드를 확인한 뒤 넣어주세요.)")
    private UUID guardianUniqueCode;
  }

  @Getter
  @Setter
  @Builder
  public static class InquiryCaregiver {
    private String name;
    private String phone;
    private String hospital;
    private String patientName;
    private List<CareerHistoryDTO> careerHistories;

    @Getter
    @Setter
    @Builder
    public static class CareerHistoryDTO {
      private String career;
    }
  }

  @Getter
  @Setter
  public static class UpdateCaregiver {
    private String caregiverName;
    private String phone;
    private String hospital;
    private List<String> carrierHistory;
    private String patientName;
    private Integer age;
    private String diseaseName;
    private String hospitalName;
    private String address;
  }

  @Getter
  @Setter
  public static class GuardianProfile {
    private String name;
    private String phone;
    private String address;
  }
}
