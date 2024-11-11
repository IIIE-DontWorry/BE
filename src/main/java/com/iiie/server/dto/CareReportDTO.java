package com.iiie.server.dto;

import com.iiie.server.dto.CareScheduleDTO.CareScheduleRequest;
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
}
