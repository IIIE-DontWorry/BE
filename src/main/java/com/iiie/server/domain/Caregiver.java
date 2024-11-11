package com.iiie.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
public class Caregiver {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "caregiver_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String hospital;

  @Column(nullable = false)
  private Long kakaoId;

  @ColumnDefault(value = "0.0")
  private Double mannerScore;

  // ===연관관계===//
  @OneToOne
  @JoinColumn(name = "patient_id", unique = true)
  private Patient patient;
}
