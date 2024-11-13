package com.iiie.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
public class Note {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "note_id")
  private Long id;

  @Column(nullable = false)
  private LocalDate createdAt;
  @Column(columnDefinition = "TEXT", nullable = false)
  private String noteContent;
  // ==시간관련==//
  @PrePersist
  private void prePersist() {
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    this.createdAt = nowInKorea.toLocalDate();
  }
  // ===연관관계===//
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "caregiver_id", nullable = false)
  private Caregiver caregiver;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "guardian_id", nullable = false)
  private Guardian guardian;
}