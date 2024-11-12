package com.iiie.server.dto;

import com.iiie.server.type.TakenStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class MedicationDTO {

  private Long id;
  private String name;
  private TakenStatus morningTakenStatus;
  private TakenStatus afternoonTakenStatus;
  private TakenStatus eveningTakenStatus;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class MedicationCheck {
    private Long id;
    private String name;
    private String morningTakenStatus;
    private String afternoonTakenStatus;
    private String eveningTakenStatus;
  }
}
