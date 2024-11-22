package com.iiie.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
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
  
  //읽기 전용
  // ===연관관계===//
  @OneToOne(mappedBy = "caregiver", cascade = CascadeType.ALL)
  private Guardian guardian;

  @OneToOne
  @JoinColumn(name = "patient_id")
  private Patient patient;

  @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @JsonIgnore
  private List<CareerHistory> careerHistories = new ArrayList<>();

  // ===연관관계 보조 메서드===//
  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void addCareerHistories(List<CareerHistory> careerHistories) {
    for (CareerHistory careerHistory : careerHistories) {
      if (this.careerHistories.contains(careerHistory)) {
        this.careerHistories.remove(careerHistory);
      }
      careerHistory.setCaregiver(this);
      this.careerHistories.add(careerHistory);
    }
  }
}
