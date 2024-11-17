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

  @Getter
  @Setter
  public static class CreationGuardian {
    private String name;
    private String phone;
    private String address;
  }
}
