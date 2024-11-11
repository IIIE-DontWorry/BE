package com.iiie.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CareSchedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "care_schedule_id")
  private Long id;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private LocalTime activityAt;

  // ===연관관계===//
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "care_report_id", nullable = false)
  @JsonIgnore
  private CareReport careReport;

  // ===연관관계보조메서드==//
  public void setCareReport(CareReport careReport) {
    this.careReport = careReport;
    if (!careReport.getCareSchedules().contains(careReport)) {
      careReport.getCareSchedules().add(this);
    }
  }
}
