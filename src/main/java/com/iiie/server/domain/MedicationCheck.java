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
public class MedicationCheckList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "medication_checklist_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  private Boolean morningTakenStatus;

  private Boolean afternoonTakenStatus;

  private Boolean eveningTakenStatus;

  // ===연관관계===//
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "patient_id", nullable = false)
  private Patient patient;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "care_report_id")
  private CareReport careReport;

  // === 연관관계 보조 메서드 === //
  public void setCareReportAndPatient(CareReport careReport, Patient patient) {
    if (!careReport.getMedicationCheckLists().contains(this)) {
      this.careReport = careReport;
      careReport.getMedicationCheckLists().add(this);
    }

    if (!patient.getMedicationCheckLists().contains(this)) {
      this.patient = patient;
      patient.getMedicationCheckLists().add(this);
    }
  }

  // === 보조 메서드 === //
  public void changeMorningTakenStatus(Boolean flag) {
    this.morningTakenStatus = flag;
  }
    public void changeAfternoonTakenStatus(Boolean flag) {
    this.afternoonTakenStatus = flag;
  }
    public void changeEveningTakenStatus(Boolean flag) {
    this.eveningTakenStatus = flag;
  }

}
