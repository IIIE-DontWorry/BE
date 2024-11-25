package com.iiie.server.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

public class NoteDTO {
  private Long id;
  private String createdBy;
  private LocalDate createdAt;
  private String noteContent;

  @Getter
  @Setter
  public static class InquiryRequest {
    private Long guardianId;
    private Long careGiverId;
  }

  @Getter
  @Setter
  public static class AddRequest {
    private Long guardianId;
    private Long careGiverId;
    private String noteContent;
  }

  @Getter
  @Setter
  public static class NoteResponse {
    private Long id;
    private String createdBy;
    private LocalDate createdAt;
    private String noteContent;
  }

  @Getter
  @Setter
  public static class DeleteNote {
    private Long id;
  }
}
