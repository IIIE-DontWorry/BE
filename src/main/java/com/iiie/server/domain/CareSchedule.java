package com.iiie.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
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
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
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
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "care_report_id")
  @JsonIgnore
  private CareReport careReport;

  // === 연관관계 보조 메서드 === //
  public void setCareReport(CareReport careReport) {
    this.careReport = careReport;
  }

  public void updateFields(String description, LocalTime activityAt) {
    this.description = description;
    this.activityAt = activityAt;
  }
}
