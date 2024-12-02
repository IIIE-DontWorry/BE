package com.iiie.server.service;

import com.iiie.server.domain.Guardian;
import com.iiie.server.domain.MedicationCheck;
import com.iiie.server.dto.MedicationCheckDTO.MedicationCheckCreationRequest;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.GuardianRepository;
import com.iiie.server.repository.MedicationCheckRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MedicationCheckService {

  private final MedicationCheckRepository medicationCheckRepository;
  private final GuardianRepository guardianRepository;

  public MedicationCheckService(
      MedicationCheckRepository medicationCheckRepository, GuardianRepository guardianRepository) {
    this.medicationCheckRepository = medicationCheckRepository;
    this.guardianRepository = guardianRepository;
  }

  @Transactional
  public void createMedicationChecks(Long guardianId, MedicationCheckCreationRequest request) {
    Guardian guardian =
        guardianRepository
            .findById(guardianId)
            .orElseThrow(() -> new NotFoundException("guardian", guardianId, "존재하지 않는 보호자 ID입니다."));

    Optional<List<MedicationCheck>> optionalMedicationChecks =
        medicationCheckRepository.findAllByPatientId(guardian.getPatient().getId());

    if (optionalMedicationChecks.isPresent()) {
      List<MedicationCheck> medicationChecks = optionalMedicationChecks.get();
      medicationChecks.forEach(MedicationCheck::changeToOld);
    }

    List<MedicationCheck> medicationChecks =
        request.getNames().stream()
            .map(
                r -> {
                  MedicationCheck medicationCheck =
                      MedicationCheck.builder()
                          .name(r)
                          .morningTakenStatus(false)
                          .afternoonTakenStatus(false)
                          .eveningTakenStatus(false)
                          .build();
                  medicationCheck.setPatient(guardian.getPatient());
                  return medicationCheck;
                })
            .toList();
    medicationCheckRepository.saveAll(medicationChecks);
  }
}
