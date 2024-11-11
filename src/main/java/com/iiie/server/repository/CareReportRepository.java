package com.iiie.server.repository;

import com.iiie.server.domain.CareReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareReportRepository extends JpaRepository<CareReport, Long> {

}
