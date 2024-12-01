package com.iiie.server.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
@Getter
public class MealExcretion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "meal_excretion_id")
  private Long id;

  // 식사활동여부
  private Boolean mealMorningTakenStatus;

  private Boolean mealAfternoonTakenStatus;

  private Boolean mealEveningTakenStatus;

  // 배변활동여부
  private Boolean excretionMorningTakenStatus;

  private Boolean excretionAfternoonTakenStatus;

  private Boolean excretionEveningTakenStatus;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "patient_id")
  private Patient patient;
}
