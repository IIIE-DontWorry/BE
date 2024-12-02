package com.iiie.server.repository;

import com.iiie.server.domain.Gallery;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
  @Query("SELECT g FROM Gallery g WHERE g.patient.id = :patientId ORDER BY g.createdAt DESC")
  List<Gallery> findTop3ByPatientIdOrderByCreatedAtDesc(@Param("patientId") Long patientId);
}
