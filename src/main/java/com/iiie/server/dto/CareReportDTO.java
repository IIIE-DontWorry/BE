package com.iiie.server.dto;

import com.iiie.server.dto.CareScheduleDTO.PatchCareScheduleRequest;
import com.iiie.server.dto.GuardianRequestDTO.GuardianRequest;
import com.iiie.server.dto.MealExcretionDTO.MealExcretionRequest;
import com.iiie.server.dto.MedicationDTO.MedicationCheckRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CareReportDTO {
  private Long id;
  private String specialNote;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Getter
  @Setter
  @Builder
  public static class CareReportResponse {
    private Long id;
    private List<CareScheduleResponse> careScheduleResponses;
    private String specialNote;
    private List<GuardianResponse> guardianResponses;
    private List<MedicationCheckResponse> medicationCheckResponse;
    private MealExcretionResponse mealExcretionResponse;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate postedDate;

    @Getter
    @Setter
    @Builder
    public static class MealExcretionResponse {
      // 식사활동여부
      private Boolean mealMorningTakenStatus;
      private Boolean mealAfternoonTakenStatus;
      private Boolean mealEveningTakenStatus;

      // 배변활동여부
      private Boolean excretionMorningTakenStatus;
      private Boolean excretionAfternoonTakenStatus;
      private Boolean excretionEveningTakenStatus;
    }

    @Getter
    @Setter
    @Builder
    public static class MedicationCheckResponse {
      private Long id;
      private String name;
      private Boolean morningTakenStatus;
      private Boolean afternoonTakenStatus;
      private Boolean eveningTakenStatus;
    }

    @Getter
    @Setter
    @Builder
    public static class CareScheduleResponse {
      private Long id;
      private String description;
      private LocalTime activityAt;
    }

    @Getter
    @Setter
    @Builder
    public static class GuardianResponse {
      private Long id;
      private String request;
      private Boolean isCheck;
    }
  }

  @Getter
  @Setter
  public static class CareReportPatchRequest {
    private List<PatchCareScheduleRequest> patchCareScheduleRequests;
    private List<MedicationCheckRequest> medicationCheckRequests;
    private MealExcretionRequest mealExcretionRequest;
    private String specialNote;
    private List<GuardianRequest> guardianRequests;
    private String postedDate;
  }
}
