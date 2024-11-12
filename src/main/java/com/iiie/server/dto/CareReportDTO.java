package com.iiie.server.dto;

import com.iiie.server.dto.CareScheduleDTO.CareScheduleRequest;
import com.iiie.server.dto.GuardianRequestDTO.GuardianRequest;
import com.iiie.server.dto.MedicationDTO.MedicationCheck;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class CareReportDTO {
  private Long id;
  private String specialNote;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Getter
  @Setter
  public static class CareReportResponse {
    private Long id;
    private String specialNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }

  @Getter
  @Setter
  public static class CareReportRequest {
    private String specialNote;
    private List<CareScheduleRequest> careSchedules; // Care_Schedule
  }

  @Getter
  @Setter
  public static class CareReportPatchRequest {
    private List<CareScheduleRequest> careScheduleRequests;
    private List<MedicationCheck> medicationChecks;
    private String specialNote;
    private List<GuardianRequest> guardianRequests;
    private String postedDate;
  }
}
