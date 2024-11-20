package com.iiie.server.convertor;

import com.iiie.server.domain.CareReport;
import com.iiie.server.domain.CareSchedule;
import com.iiie.server.domain.MedicationCheckList;
import com.iiie.server.dto.CareReportDTO.CareReportResponse;
import com.iiie.server.dto.CareReportDTO.CareReportResponse.CareScheduleResponse;
import com.iiie.server.dto.CareReportDTO.CareReportResponse.GuardianResponse;
import com.iiie.server.dto.CareReportDTO.CareReportResponse.MedicationCheckResponse;
import java.util.List;

public class Convertor {

  public static CareReportResponse toCareReportResponse(CareReport careReport) {
    return CareReportResponse.builder()
        .id(careReport.getId())
        .careScheduleResponses(toCareScheduleResponse(careReport.getCareSchedules()))
        .specialNote(careReport.getSpecialNote())
        .guardianResponses(toGuardianResponse(careReport.getGuardianRequests()))
        .medicationCheckResponse(toMedicationCheckResponse(careReport.getMedicationCheckLists()))
        .createdAt(careReport.getCreatedAt())
        .updatedAt(careReport.getUpdatedAt())
        .postedDate(careReport.getPostedDate())
        .build();
  }

  private static List<MedicationCheckResponse> toMedicationCheckResponse(
      List<MedicationCheckList> medicationCheckLists) {
    return medicationCheckLists.stream()
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

  private static List<CareScheduleResponse> toCareScheduleResponse(
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

  private static List<GuardianResponse> toGuardianResponse(
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
}
