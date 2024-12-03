package com.iiie.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

  @ColumnDefault(value = "36.5")
  private Double mannerScore;

  // 읽기 전용
  // ===연관관계===//
  @OneToOne(mappedBy = "caregiver")
  private Guardian guardian;

  @OneToOne
  @JoinColumn(name = "patient_id")
  private Patient patient;

  @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL)
  @Builder.Default
  @JsonIgnore
  private List<CareerHistory> careerHistories = new ArrayList<>();

  // ==초기 설정==//
  @PrePersist
  public void prePersist() {
    this.mannerScore = 36.5;
  }

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

  // 매너 점수 수정 매서드
  public void updateMannerScore(double delta) {
    this.mannerScore += delta;
  }
}
