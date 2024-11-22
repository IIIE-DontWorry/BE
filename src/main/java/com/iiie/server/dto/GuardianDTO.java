package com.iiie.server.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

public class GuardianDTO {
  private Long id;
  private String name;
  private String phone;
  private String address;
  private Long kakaoId;
  private Double mannerScore;
  private UUID uniqueCode;
  private String patientName;

  @Getter
  @Setter
  public static class CreationGuardian {
    private String name;
    private String phone;
    private String address;
  }

  @Getter
  @Setter
  public static class InquiryGuardian {
    private String name;
    private String phone;
    private String address;
    private String patientName;
  }

  @Getter
  @Setter
  public static class UpdateGuardian {
    private String name;
    private String phone;
    private String address;
  }

  @Getter
  @Setter
  public static class CaregiverProfile {
    private String name;
    private String phone;
    private String hospital;
  }
}
