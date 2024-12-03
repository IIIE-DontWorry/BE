package com.iiie.server.domain;

import com.iiie.server.type.RelationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PatientGuardianRelation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "patient_guardian_relation_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RelationType relationType;
}
