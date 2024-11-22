package com.iiie.server.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Gallery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "gallery_id")
  private Long id;

  // ===연관관계===//
  @OneToOne
  @JoinColumn(name = "patient_id")
  private Patient patient;

  @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Image> images = new ArrayList<>();
}
