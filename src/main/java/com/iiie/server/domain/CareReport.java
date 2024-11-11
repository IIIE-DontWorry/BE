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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareReport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "care_report_id")
  private Long id;

  @Column(nullable = false)
  private String specialNote;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  //==시간관련==//
  @PrePersist
  private void prePersist() {
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    this.createdAt = nowInKorea.toLocalDateTime();
    this.updatedAt = nowInKorea.toLocalDateTime();
  }

  @PreUpdate
  private void preUpdate() {
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    this.updatedAt = nowInKorea.toLocalDateTime();
  }

  // ===연관관계===/
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "caregiver_id", nullable = false)
  private Caregiver caregiver;

  @OneToMany(mappedBy = "careReport", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<CareSchedule> careSchedules = new ArrayList<>();

  @OneToMany(mappedBy = "careReport", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<MedicationCheckList> medicationCheckLists = new ArrayList<>();

  @OneToMany(mappedBy = "careReport", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<GuardianRequest> guardianRequests = new ArrayList<>();
}
