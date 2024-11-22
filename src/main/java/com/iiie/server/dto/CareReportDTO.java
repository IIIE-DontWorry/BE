package com.iiie.server.dto;

import com.iiie.server.dto.CareScheduleDTO.CareScheduleRequest;
import com.iiie.server.dto.GuardianRequestDTO.GuardianRequest;
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
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate postedDate;

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
    private List<CareScheduleRequest> careScheduleRequests;
    private List<MedicationCheckRequest> medicationCheckRequests;
    private String specialNote;
    private List<GuardianRequest> guardianRequests;
    private String postedDate;
  }
}
