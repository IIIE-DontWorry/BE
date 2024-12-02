package com.iiie.server.service;

import com.iiie.server.convertor.ConvertorDTO;
import com.iiie.server.convertor.EntityUpdater;
import com.iiie.server.domain.CareReport;
import com.iiie.server.domain.CareSchedule;
import com.iiie.server.domain.Caregiver;
import com.iiie.server.domain.Guardian;
import com.iiie.server.domain.GuardianRequest;
import com.iiie.server.domain.MealExcretion;
import com.iiie.server.domain.MedicationCheck;
import com.iiie.server.dto.CareReportDTO.CareReportPatchRequest;
import com.iiie.server.dto.CareReportDTO.CareReportResponse;
import com.iiie.server.dto.MealExcretionDTO.MealExcretionRequest;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.CareReportRepository;
import com.iiie.server.repository.CareScheduleRepository;
import com.iiie.server.repository.CaregiverRepository;
import com.iiie.server.repository.GuardianRequestRepository;
import com.iiie.server.repository.MedicationCheckRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CareReportService {

  private final CareReportRepository careReportRepository;
  private final CaregiverRepository careGiverRepository;
  private final MedicationCheckRepository medicationCheckRepository;
  private final GuardianRequestRepository guardianRequestRepository;
  private final CareScheduleRepository careScheduleRepository;

  public CareReportService(
      CareReportRepository careReportRepository,
      CaregiverRepository careGiverRepository,
      MedicationCheckRepository medicationCheckRepository,
      GuardianRequestRepository guardianRequestRepository,
      CareScheduleRepository careScheduleRepository) {
    this.careReportRepository = careReportRepository;
    this.careGiverRepository = careGiverRepository;
    this.medicationCheckRepository = medicationCheckRepository;
    this.guardianRequestRepository = guardianRequestRepository;
    this.careScheduleRepository = careScheduleRepository;
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
  public CareReportResponse initCareReport(Long careGiverId) {
    Caregiver caregiver =
        careGiverRepository
            .findById(careGiverId)
            .orElseThrow(() -> new NotFoundException("careGiver", careGiverId, "존재하지 않는 간병인입니다."));

    LocalDate today = LocalDate.now();
    Optional<CareReport> existingReport =
        careReportRepository.findByCaregiverIdAndPostedDate(careGiverId, today);

    if (existingReport.isPresent()) {
      return ConvertorDTO.toCareReportResponse(existingReport.get());
    }

    MealExcretion mealExcretion =
        MealExcretion.builder()
            .mealMorningTakenStatus(false)
            .mealAfternoonTakenStatus(false)
            .mealEveningTakenStatus(false)
            .excretionMorningTakenStatus(false)
            .excretionAfternoonTakenStatus(false)
            .excretionEveningTakenStatus(false)
            .build();

    // 최근 요청 사항만 불러와 매핑해준다.
    Guardian guardian = caregiver.getPatient().getGuardian();
    List<GuardianRequest> newGuardianRequest =
        guardianRequestRepository.findAllByIsNewTrueAndGuardianId(guardian.getId());

    // CareReport 엔티티 생성
    CareReport careReport = CareReport.builder().caregiver(caregiver).specialNote("").build();
    careReport.setMealExcretion(mealExcretion);
    careReport.addGuardianRequests(newGuardianRequest);

    CareReportResponse result =
        ConvertorDTO.toCareReportResponse(careReportRepository.save(careReport));

    return result;
  }

  public CareReportResponse getCareReportDetail(Long careReportId) {

    CareReport careReport =
        careReportRepository
            .findById(careReportId)
            .orElseThrow(
                () -> new NotFoundException("careReport", careReportId, "존재하지 않는 간병 보고서 입니다."));

    return ConvertorDTO.toCareReportResponse(careReport);
  }

  @Transactional
  public void deleteCareReport(Long careReportId) {
    careReportRepository
        .findById(careReportId)
        .orElseThrow(
            () -> new NotFoundException("careReport", careReportId, "존재하지 않는 간병보고서이므로 삭제 불가능합니다."));

    careReportRepository.deleteById(careReportId);
  }

  // TODO 1 :  init하고 날짜 변경 시 -> 예외 뜬다. (해결)
  // TODO 2 :  존재하지 않는 날짜 시 -> init으로 생성해준 뒤 + 수정까지.
  @Transactional
  public CareReportResponse patchCareReport(Long careReportId, CareReportPatchRequest request) {

    final LocalDate postedDate = LocalDate.parse(request.getPostedDate());
    CareReport careReport =
        careReportRepository
            .findByCaregiverIdAndPostedDate(careReportId, postedDate)
            .orElseThrow(() -> new NotFoundException("care_report", null, "존재하지 않는 간병 보고서입니다."));

    if (!request.getPatchCareScheduleRequests().isEmpty()) {
      // 시간에 따른 일정 엔티티 업데이트
      List<CareSchedule> careScheduleList =
          EntityUpdater.toCareScheduleList(
              request.getPatchCareScheduleRequests(), careScheduleRepository);
      careScheduleList.forEach(careReport::setCareSchedules); // 연관관계 매핑
    }

    // 배변활동 및 식사 여부 업데이트
    MealExcretion mealExcretion = careReport.getMealExcretion();
    MealExcretionRequest mealExcretionRequest = request.getMealExcretionRequest();

    mealExcretion.setMealMorningTakenStatus(mealExcretionRequest.getMealMorningTakenStatus());
    mealExcretion.setMealAfternoonTakenStatus(mealExcretionRequest.getMealAfternoonTakenStatus());
    mealExcretion.setMealEveningTakenStatus(mealExcretionRequest.getMealEveningTakenStatus());

    mealExcretion.setExcretionMorningTakenStatus(
        mealExcretionRequest.getExcretionMorningTakenStatus());
    mealExcretion.setExcretionAfternoonTakenStatus(
        mealExcretionRequest.getExcretionAfternoonTakenStatus());
    mealExcretion.setExcretionEveningTakenStatus(
        mealExcretionRequest.getExcretionEveningTakenStatus());

    if (!request.getMedicationCheckRequests().isEmpty()) {
      // 투약 리스트 엔티티 업데이트
      List<MedicationCheck> medicationCheckList =
          EntityUpdater.toMedicationCheckList(
              request.getMedicationCheckRequests(), medicationCheckRepository);
      medicationCheckList.forEach(
          medicationCheck ->
              medicationCheck.setCareReportAndPatient(
                  careReport, careReport.getCaregiver().getPatient()));
    }

    if (!request.getGuardianRequests().isEmpty()) {
      // 보호자 요청사항 엔티티 업데이트
      List<GuardianRequest> guardianRequestList =
          EntityUpdater.toGuardianRequestList(
              request.getGuardianRequests(), guardianRequestRepository);
      guardianRequestList.forEach(
          guardianRequest -> {
            guardianRequest.setGuardian(careReport.getCaregiver().getGuardian());
            guardianRequest.setCareReport(careReport);
          });
    }

    // 간병보고서 엔티티 업데이트 및 저장 및 반환 with specialNote, PostedDate
    if (!request.getSpecialNote().isEmpty()) {
      careReport.changePostedDate(request.getPostedDate());
    }

    if (!request.getPostedDate().isEmpty()) {
      careReport.changeSpecialNote(request.getSpecialNote());
    }

    return ConvertorDTO.toCareReportResponse(careReport);
  }

  public Page<CareReportResponse> getAllCareReports(Long careGiverId, Pageable pageable) {
    careGiverRepository
        .findById(careGiverId)
        .orElseThrow(() -> new NotFoundException("caregiver", careGiverId, "존재하지 않는 간병인입니다."));

    Page<CareReport> careReports = careReportRepository.findAllByCaregiverId(pageable, careGiverId);
    return careReports.map(ConvertorDTO::toCareReportResponse);
  }
}
