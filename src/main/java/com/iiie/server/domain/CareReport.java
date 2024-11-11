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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

  @ColumnDefault(value = "")
  private String specialNote;

  @Column(nullable = false)
  private LocalDate createdAt;

  @Column(nullable = false)
  private LocalDate updatedAt;

  // ==시간관련==//
  @PrePersist
  private void prePersist() {
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    this.createdAt = nowInKorea.toLocalDate();
    this.updatedAt = nowInKorea.toLocalDate();
  }

  @PreUpdate
  private void preUpdate() {
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    this.updatedAt = nowInKorea.toLocalDate();
  }

  // ===연관관계===/
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "caregiver_id", nullable = false)
  private Caregiver caregiver;

  @Getter
  @OneToMany(
      mappedBy = "careReport",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @Builder.Default
  private List<CareSchedule> careSchedules = new ArrayList<>();

  @OneToMany(
      mappedBy = "careReport",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @Builder.Default
  private List<MedicationCheckList> medicationCheckLists = new ArrayList<>();

  @OneToMany(
      mappedBy = "careReport",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @Builder.Default
  private List<GuardianRequest> guardianRequests = new ArrayList<>();
}
