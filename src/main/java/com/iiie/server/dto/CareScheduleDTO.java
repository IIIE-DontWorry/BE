package com.iiie.server.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

public class CareScheduleDTO {

  private Long id;
  private String description;
  private LocalDateTime activityAt;

  @Getter
  @Setter
  public static class CareScheduleRequest {
    private String description; // 일지 내용
    private int year;
    private int hour;
    private int minute;
  }
}
