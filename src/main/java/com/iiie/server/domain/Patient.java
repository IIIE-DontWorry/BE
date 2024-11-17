package com.iiie.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
  private Long kakaoId;

  // ===연관관계===//
  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<MedicationCheckList> medicationCheckLists = new ArrayList<>();
}
