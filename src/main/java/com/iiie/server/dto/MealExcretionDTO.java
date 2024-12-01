package com.iiie.server.dto;

import lombok.Builder;
import lombok.Data;

public class MealExcretionDTO {

  @Data
  @Builder
  public static class MealExcretionRequest {
    // 식사활동여부
    private Boolean mealMorningTakenStatus;

    private Boolean mealAfternoonTakenStatus;

    private Boolean mealEveningTakenStatus;

    // 배변활동여부
    private Boolean excretionMorningTakenStatus;

    private Boolean excretionAfternoonTakenStatus;

    private Boolean excretionEveningTakenStatus;
  }
}
