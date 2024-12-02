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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
public class GuardianRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "guardian_request_id")
  private Long id;

  @Column(nullable = false)
  private String request;

  @Column(nullable = true)
  private Boolean isCheck;

  @Column private Boolean isNew;

  @Column private LocalDateTime createdAt;

  @Column private LocalDateTime updatedAt;

  @PrePersist
  private void prePersist() {
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    this.createdAt = nowInKorea.toLocalDateTime();
    this.updatedAt = nowInKorea.toLocalDateTime();
    this.isNew = true;
  }

  @PreUpdate
  private void preUpdate() {
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    this.updatedAt = nowInKorea.toLocalDateTime();
  }

  // ===연관관계===//
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "guardian_id")
  private Guardian guardian;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "care_report_id")
  private CareReport careReport;

  // === 연관관계 보조 메서드 === //
  public void setCareReport(CareReport careReport) {
    if (!careReport.getGuardianRequests().contains(this)) {
      this.careReport = careReport;
      careReport.getGuardianRequests().add(this);
    }
  }

  public void setGuardian(Guardian guardian) {
    if (!guardian.getGuardianRequests().contains(this)) {
      this.guardian = guardian;
      guardian.getGuardianRequests().add(this);
    }
  }

  // === 보조 메서드 === //
  public void updateFields(String request, Boolean isCheck) {
    this.request = request;
    this.isCheck = isCheck;
  }

  public void changeToOld() {
    this.isNew = false;
  }
}
