package com.iiie.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "patient_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer age;

  @Column(nullable = false)
  private String diseaseName;

  @Column(nullable = false)
  private String hospitalName;

  @Column(nullable = true)
  private String address;

  @Column(nullable = true)
  private Long kakaoId;

  // ===연관관계===//
  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @JsonIgnore
  private List<MedicationCheck> medicationChecks = new ArrayList<>();

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
  private Guardian guardian;

  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Gallery> galleries = new ArrayList<>();

  // === 연관관계 보조 메서드 === //
  public void addMedicationChecks(List<MedicationCheck> medicationChecks) {
    for (MedicationCheck medicationCheck : medicationChecks) {
      if (!this.medicationChecks.contains(medicationCheck)) {
        this.medicationChecks.add(medicationCheck);
        medicationCheck.setPatient(this);
      }
    }
  }
}
