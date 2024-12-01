package com.iiie.server.convertor;

import com.iiie.server.domain.CareReport;
import com.iiie.server.domain.CareSchedule;
import com.iiie.server.domain.MedicationCheck;
import com.iiie.server.dto.CareReportDTO.CareReportResponse;
import com.iiie.server.dto.CareReportDTO.CareReportResponse.CareScheduleResponse;
import com.iiie.server.dto.CareReportDTO.CareReportResponse.GuardianResponse;
import com.iiie.server.dto.CareReportDTO.CareReportResponse.MedicationCheckResponse;
import com.iiie.server.dto.GuardianDTO.InquiryGuardian.PatientInfo.MedicationInfo;
import java.util.List;

public class ConvertorDTO {

  public static CareReportResponse toCareReportResponse(CareReport careReport) {
    return CareReportResponse.builder()
        .id(careReport.getId())
        .careScheduleResponses(toCareScheduleResponses(careReport.getCareSchedules()))
        .specialNote(careReport.getSpecialNote())
        .guardianResponses(toGuardianResponses(careReport.getGuardianRequests()))
        .medicationCheckResponse(
            toMedicationCheckResponses(
                careReport.getCaregiver().getPatient().getMedicationChecks()))
        .createdAt(careReport.getCreatedAt())
        .updatedAt(careReport.getUpdatedAt())
        .postedDate(careReport.getPostedDate())
        .build();
  }

  private static List<MedicationCheckResponse> toMedicationCheckResponses(
      List<MedicationCheck> medicationChecks) {
    return medicationChecks.stream()
        .map(
            medication ->
                MedicationCheckResponse.builder()
                    .id(medication.getId())
                    .name(medication.getName())
                    .morningTakenStatus(medication.getMorningTakenStatus())
                    .afternoonTakenStatus(medication.getAfternoonTakenStatus())
                    .eveningTakenStatus(medication.getEveningTakenStatus())
                    .build())
        .toList();
  }

  private static List<CareScheduleResponse> toCareScheduleResponses(
      List<CareSchedule> careSchedules) {
    return careSchedules.stream()
        .map(
            schedule ->
                CareScheduleResponse.builder()
                    .id(schedule.getId())
                    .activityAt(schedule.getActivityAt())
                    .description(schedule.getDescription())
                    .build())
        .toList();
  }

  public static CareScheduleResponse toCareScheduleResponse(CareSchedule careSchedule) {
    return CareScheduleResponse.builder()
        .id(careSchedule.getId())
        .description(careSchedule.getDescription())
        .activityAt(careSchedule.getActivityAt())
        .build();
  }

  private static List<GuardianResponse> toGuardianResponses(
      List<com.iiie.server.domain.GuardianRequest> guardianRequests) {
    return guardianRequests.stream()
        .map(
            request ->
                GuardianResponse.builder()
                    .id(request.getId())
                    .request(request.getRequest())
                    .isCheck(request.getIsCheck())
                    .build())
        .toList();
  }

  private static MedicationInfo toMedicationInfo(MedicationCheck medicationCheck) {
    return MedicationInfo.builder().name(medicationCheck.getName()).build();
  }

  public static List<MedicationInfo> toMedicationInfos(List<MedicationCheck> medicationChecks) {
    return medicationChecks.stream().map(ConvertorDTO::toMedicationInfo).toList();
  }
}
