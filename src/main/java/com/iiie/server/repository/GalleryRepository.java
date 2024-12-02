package com.iiie.server.repository;

import com.iiie.server.domain.Gallery;
import com.iiie.server.domain.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
  // 환자의 최근 이미지 3개 조회
  @Query("SELECT i FROM Image i WHERE i.gallery.patient.id = :patientId ORDER BY i.id DESC")
  List<Image> findTop3ImagesByPatientIdOrderByIdDesc(@Param("patientId") Long patientId);
}
