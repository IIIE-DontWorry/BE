package com.iiie.server.repository;

import com.iiie.server.domain.CareReport;
import java.time.LocalDate;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareReportRepository extends JpaRepository<CareReport, Long> {

  Optional<CareReport> findByCaregiverIdAndPostedDate(Long careGiverId, LocalDate today);

  Page<CareReport> findAllByCaregiverId(@NonNull Pageable pageable, Long careGiverId);

  Optional<CareReport> findByIdAndPostedDate(Long careReportId, LocalDate postedDate);
}
