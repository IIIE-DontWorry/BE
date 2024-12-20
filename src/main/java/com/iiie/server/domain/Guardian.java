package com.iiie.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Guardian {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "guardian_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private Long kakaoId;

  @ColumnDefault(value = "36.5")
  private Double mannerScore;

  @Column(nullable = false)
  private UUID uniqueCode;

  // ===연관관계===//
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "caregiver_id", nullable = true)
  private Caregiver caregiver;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "patient_id")
  private Patient patient;

  @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @JsonIgnore
  private List<GuardianRequest> guardianRequests = new ArrayList<>();

  // ==초기 설정==//
  @PrePersist
  public void prePersist() {
    this.mannerScore = 36.5;
  }

  // ===연관관계 보조 메서드===//
  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void setCaregiver(Caregiver caregiver) {
    this.caregiver = caregiver;
  }

  public void updateMannerScore(Double delta) {
    this.mannerScore += delta;
  }
}
