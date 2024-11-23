package com.iiie.server.convertor;

import com.iiie.server.domain.CareSchedule;
import com.iiie.server.domain.GuardianRequest;
import com.iiie.server.domain.MedicationCheck;
import com.iiie.server.dto.CareScheduleDTO.CareScheduleRequest;
import com.iiie.server.dto.GuardianRequestDTO;
import com.iiie.server.dto.MedicationDTO.MedicationCheckRequest;
import com.iiie.server.repository.CareScheduleRepository;
import com.iiie.server.repository.GuardianRequestRepository;
import com.iiie.server.repository.MedicationCheckRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUpdater {

  public static CareSchedule toCareSchedule(
      CareScheduleRequest dto, CareScheduleRepository careScheduleRepository) {
    CareSchedule careSchedule = careScheduleRepository.findById(dto.getId()).get();
    careSchedule.updateFields(dto.getDescription(), LocalTime.of(dto.getHour(), dto.getMinute()));
    return careSchedule;
  }

  public static List<CareSchedule> toCareScheduleList(
      List<CareScheduleRequest> dtos, CareScheduleRepository careScheduleRepository) {

    return dtos.stream()
        .map(dto -> EntityUpdater.toCareSchedule(dto, careScheduleRepository))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public static MedicationCheck toMedicationCheck(
      MedicationCheckRequest dto, MedicationCheckRepository medicationCheckRepository) {

    MedicationCheck medicationCheck = medicationCheckRepository.findById(dto.getId()).get();
    medicationCheck.updateFields(
        dto.getMorningTakenStatus(), dto.getAfternoonTakenStatus(), dto.getEveningTakenStatus());

    return medicationCheck;
  }

  public static List<MedicationCheck> toMedicationCheckList(
      List<MedicationCheckRequest> dtos, MedicationCheckRepository medicationCheckRepository) {
    return dtos.stream()
        .map(dto -> EntityUpdater.toMedicationCheck(dto, medicationCheckRepository))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public static GuardianRequest toGuardianRequest(
      GuardianRequestDTO.GuardianRequest dto, GuardianRequestRepository guardianRequestRepository) {

    GuardianRequest guardianRequest = guardianRequestRepository.findById(dto.getId()).get();
    guardianRequest.updateFields(dto.getRequest(), dto.getIsCheck());
    return guardianRequest;
  }

  public static List<GuardianRequest> toGuardianRequestList(
      List<GuardianRequestDTO.GuardianRequest> dtos,
      GuardianRequestRepository guardianRequestRepository) {
    return dtos.stream()
        .map(dto -> EntityUpdater.toGuardianRequest(dto, guardianRequestRepository))
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
