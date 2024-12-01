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
  public static class PatchCareScheduleRequest {
    private Long id;
    private String description; // 일지 내용
    private int hour;
    private int minute;
  }

  @Getter
  @Setter
  public static class PostCareScheduleRequest {
    private String description; // 일지 내용
    private int hour;
    private int minute;
  }
}
