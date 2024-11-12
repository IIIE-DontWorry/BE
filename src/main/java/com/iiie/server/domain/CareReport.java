package com.iiie.server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @Column(nullable = false)
  private LocalDate postedDate;

  // ==시간관련==//
  @PrePersist
  private void prePersist() {
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    this.createdAt = nowInKorea.toLocalDate();
    this.updatedAt = nowInKorea.toLocalDate();
    this.postedDate = nowInKorea.toLocalDate();
  }

  @PreUpdate
  private void preUpdate() {
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    this.updatedAt = nowInKorea.toLocalDate();
  }

  // ===연관관계===/
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "caregiver_id", nullable = false)
  private Caregiver caregiver;

  @Getter
  @OneToMany(
      mappedBy = "careReport",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @Builder.Default
  @JsonIgnore
  private List<CareSchedule> careSchedules = new ArrayList<>();

  @OneToMany(
      mappedBy = "careReport",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @Builder.Default
  @JsonIgnore
  private List<MedicationCheckList> medicationCheckLists = new ArrayList<>();

  @OneToMany(
      mappedBy = "careReport",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @Builder.Default
  @JsonIgnore
  private List<GuardianRequest> guardianRequests = new ArrayList<>();

  // ===연관관계 보조 메서드===//
  public void updateSpecialNote(String specialNote) {
    this.specialNote = specialNote;
  }

  public void addCareSchedule(CareSchedule careSchedule) {
    this.careSchedules.add(careSchedule);
    careSchedule.setCareReport(this);
  }

  public void addMedicationCheckList(MedicationCheckList medicationCheckList) {
    this.medicationCheckLists.add(medicationCheckList);
    medicationCheckList.setCareReport(this);
  }

  public void addGuardianRequest(GuardianRequest guardianRequest) {
    this.guardianRequests.add(guardianRequest);
    guardianRequest.setCareReport(this);
  }

  // ===보조 메서드===//
  public void updateCareSchedules(List<CareSchedule> newCareSchedules) {
    this.careSchedules.clear();

    for (CareSchedule careSchedule : newCareSchedules) {
      addCareSchedule(careSchedule);
    }
  }

  public void updateMedicationCheckLists(List<MedicationCheckList> newMedicationCheckLists) {
    this.medicationCheckLists.clear();
    for (MedicationCheckList medicationCheckList : newMedicationCheckLists) {
      addMedicationCheckList(medicationCheckList);
    }
  }

  public void updateGuardianRequests(List<GuardianRequest> guardianRequests) {
    this.guardianRequests.clear();
    for (GuardianRequest guardianRequest : guardianRequests) {
      addGuardianRequest(guardianRequest);
    }
  }

  public void changePostedDate(String postedDate) {
    this.postedDate = LocalDate.parse(postedDate);
  }
}
