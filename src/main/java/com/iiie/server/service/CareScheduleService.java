package com.iiie.server.service;

import com.iiie.server.convertor.ConvertorDTO;
import com.iiie.server.domain.CareReport;
import com.iiie.server.domain.CareSchedule;
import com.iiie.server.dto.CareReportDTO.CareReportResponse.CareScheduleResponse;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.CareReportRepository;
import com.iiie.server.repository.CareScheduleRepository;
import java.time.LocalTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CareScheduleService {

  private final CareScheduleRepository careScheduleRepository;
  private final CareReportRepository careReportRepository;

  public CareScheduleService(
      CareScheduleRepository careScheduleRepository, CareReportRepository careReportRepository) {
    this.careScheduleRepository = careScheduleRepository;
    this.careReportRepository = careReportRepository;
  }

  @Transactional
  public CareScheduleResponse createCareSchedule(
      Long careReportId, int hour, int minute, String description) {

    CareReport careReport =
        careReportRepository
            .findById(careReportId)
            .orElseThrow(
                () -> new NotFoundException("careReport", careReportId, "존재하지 않는 간병보고서입니다."));

    CareSchedule careSchedule =
        CareSchedule.builder()
            .description(description)
            .activityAt(LocalTime.of(hour, minute))
            .build();
    careReport.setCareSchedules(careSchedule);

    CareSchedule schedule = careScheduleRepository.save(careSchedule);
    return ConvertorDTO.toCareScheduleResponse(schedule);
  }
}
