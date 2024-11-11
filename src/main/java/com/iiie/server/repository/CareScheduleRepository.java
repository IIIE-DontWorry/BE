package com.iiie.server.repository;

import com.iiie.server.domain.CareSchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareScheduleRepository extends JpaRepository<CareSchedule, Long> {

  List<CareSchedule> findAllByCareReportId(Long careReportId);
}
