package com.iiie.server.dto;

import lombok.Getter;
import lombok.Setter;

public class PatientDTO {

  private Long id;
  private String name;
  private Integer age;
  private String diseaseName;
  private String hospitalName;

  @Getter
  @Setter
  public static class CreationPatient {
    private String name;
    private Integer age;
    private String diseaseName;
    private String hospitalName;
  }
}
