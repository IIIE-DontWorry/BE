package com.iiie.server.repository;

import com.iiie.server.domain.CareReport;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareReportRepository extends JpaRepository<CareReport, Long> {

  Optional<CareReport> findByCaregiverIdAndPostedDate(Long careGiverId, LocalDate today);
}
