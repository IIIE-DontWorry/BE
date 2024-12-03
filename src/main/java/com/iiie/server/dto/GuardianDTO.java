package com.iiie.server.dto;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
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
  @Builder
  public static class InquiryGuardian {

    private GuardianInfo guardianInfo;
    private PatientInfo patientInfo;
    private UUID uniqueCode;

    @Getter
    @Setter
    @Builder
    public static class GuardianInfo {

      private String name;
      private String phone;
      private String address;
    }

    @Getter
    @Setter
    @Builder
    public static class PatientInfo {

      private String name;
      private Integer age;
      private String diseaseName;
      private String hospitalName;
      private List<MedicationInfo> MedicationInfos;

      @Getter
      @Setter
      @Builder
      public static class MedicationInfo {

        private String name;
      }
    }
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
