package com.iiie.server.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Gallery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "gallery_id")
  private Long gallery_id;

  @Column(nullable = false)
  private String createdBy;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String title;

  // ==시간관련==//
  @PrePersist
  private void prePersist() {
    this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
  }

  // ===연관관계===//
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "patient_id")
  private Patient patient;

  @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Image> images = new ArrayList<>();

  public void addImages(List<Image> images) {
    for (Image image : images) {
      if(!this.images.contains(image)) {
        this.images.add(image);
        image.setGallery(this);
      }
    }
  }
}
