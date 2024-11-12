package com.iiie.server.service;

import com.iiie.server.domain.CareReport;
import com.iiie.server.domain.CareSchedule;
import com.iiie.server.domain.Caregiver;
import com.iiie.server.domain.GuardianRequest;
import com.iiie.server.domain.MedicationCheckList;
import com.iiie.server.dto.CareReportDTO.CareReportPatchRequest;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.CareGiverRepository;
import com.iiie.server.repository.CareReportRepository;
import com.iiie.server.type.TakenStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CareReportService {

  private final CareReportRepository careReportRepository;
  private final CareGiverRepository careGiverRepository;

  public CareReportService(
      CareReportRepository careReportRepository, CareGiverRepository careGiverRepository) {
    this.careReportRepository = careReportRepository;
    this.careGiverRepository = careGiverRepository;
  }

  /**
   * 오늘 날짜의 간병 보고서를 생성(초기화)합니다.
   *
   * <p>주어진 간병인 ID로 오늘 날짜의 간병 보고서가 이미 존재하는지 확인합니다. 존재하면 기존 보고서를 반환하고, 존재하지 않으면 새로운 보고서를 생성하여 반환합니다.
   *
   * @param careGiverId 간병인의 ID
   * @return 오늘 날짜의 간병 보고서 객체
   * @throws NotFoundException 간병인을 찾을 수 없는 경우 발생
   */
  @Transactional
  public CareReport initCareReport(Long careGiverId) {
    Caregiver caregiver =
        careGiverRepository
            .findById(careGiverId)
            .orElseThrow(() -> new NotFoundException("careGiver", careGiverId, "존재하지 않는 간병인입니다."));

    LocalDate today = LocalDate.now();
    Optional<CareReport> existingReport =
        careReportRepository.findByCaregiverIdAndPostedDate(careGiverId, today);

    if (existingReport.isPresent()) {
      return existingReport.get();
    }

    CareReport careReport = CareReport.builder().caregiver(caregiver).specialNote("").build();

    return careReportRepository.save(careReport);
  }

  public CareReport getCareReportDetail(Long careReportId) {

    return careReportRepository
        .findById(careReportId)
        .orElseThrow(
            () -> new NotFoundException("careReport", careReportId, "존재하지 않는 간병 보고서 입니다."));
  }

  @Transactional
  public void deleteCareReport(Long careReportId) {
    careReportRepository
        .findById(careReportId)
        .orElseThrow(
            () -> new NotFoundException("careReport", careReportId, "존재하지 않는 간병보고서이므로 삭제 불가능합니다."));

    careReportRepository.deleteById(careReportId);
  }

  @Transactional
  public CareReport patchCareReport(Long careReportId, CareReportPatchRequest request) {
    return careReportRepository
        .findById(careReportId)
        .map(
            careReport -> {
              if (request.getPostedDate() != null) {
                careReport.changePostedDate(request.getPostedDate());
              }

              if (request.getSpecialNote() != null) {
                careReport.updateSpecialNote(request.getSpecialNote());
              }

              if (request.getCareScheduleRequests() != null) {
                List<CareSchedule> newSchedule =
                    request.getCareScheduleRequests().stream()
                        .map(
                            scheduleRequest ->
                                CareSchedule.builder()
                                    .activityAt(
                                        LocalTime.of(
                                            scheduleRequest.getHour(), scheduleRequest.getMinute()))
                                    .description(scheduleRequest.getDescription())
                                    .build())
                        .toList();
                careReport.updateCareSchedules(newSchedule);
              }

              if (request.getMedicationChecks() != null) {
                List<MedicationCheckList> newCheckList =
                    request.getMedicationChecks().stream()
                        .map(
                            medicationCheck ->
                                MedicationCheckList.builder()
                                    .name(medicationCheck.getName())
                                    .morningTakenStatus(
                                        TakenStatus.fromString(
                                            medicationCheck.getMorningTakenStatus()))
                                    .afternoonTakenStatus(
                                        TakenStatus.fromString(
                                            medicationCheck.getAfternoonTakenStatus()))
                                    .eveningTakenStatus(
                                        TakenStatus.fromString(
                                            medicationCheck.getEveningTakenStatus()))
                                    .build())
                        .toList();
                careReport.updateMedicationCheckLists(newCheckList);
              }

              if (request.getGuardianRequests() != null) {
                List<GuardianRequest> guardianRequests =
                    request.getGuardianRequests().stream()
                        .map(
                            guardianRequest ->
                                GuardianRequest.builder()
                                    .request(guardianRequest.getRequest())
                                    .build())
                        .toList();
                careReport.updateGuardianRequests(guardianRequests);
              }
              return careReport;
            })
        .orElseThrow(
            () -> new NotFoundException("careReport", careReportId, "존재하지 않는 간병 보고서 입니다."));
  }
}
