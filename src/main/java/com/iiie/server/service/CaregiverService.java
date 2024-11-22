package com.iiie.server.service;

import com.iiie.server.domain.Caregiver;
import com.iiie.server.domain.Patient;
import com.iiie.server.dto.CaregiverDTO;
import com.iiie.server.repository.CareGiverRepository;
import com.iiie.server.repository.GuardianRepository;
import com.iiie.server.exception.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CaregiverService {

  private final GuardianRepository guardianRepository;
  private final CareGiverRepository careGiverRepository;

  public CaregiverService(
      GuardianRepository guardianRepository, CareGiverRepository careGiverRepository) {
    this.guardianRepository = guardianRepository;
    this.careGiverRepository = careGiverRepository;
  }
  
    public Caregiver createCaregiver(
      String name,
      String phone,
      String hospital,
      List<String> careerHistoryDescriptions,
      UUID guardianUniqueCode) {

    Guardian guardian =
        guardianRepository
            .findByUniqueCode(guardianUniqueCode)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "guardian", guardianUniqueCode, "코드와 일치하는 보호자가 존재하지 않습니다."));

    List<CareerHistory> careerHistories =
        careerHistoryDescriptions.stream()
            .map(
                careerHistoryDescription ->
                    CareerHistory.builder().description(careerHistoryDescription).build())
            .toList();

    Caregiver caregiver =
        Caregiver.builder()
            .name(name)
            .phone(phone)
            .hospital(hospital)
            .kakaoId(999L) // TODO: 임시 값
            .mannerScore(0.0)
            .careerHistories(careerHistories)
            .build();
    caregiver.setPatient(guardian.getPatient());
    caregiver.addCareerHistories(careerHistories);

    return careGiverRepository.save(caregiver);
  }

  @Transactional(readOnly = true)
  public CaregiverDTO.InquiryCaregiver inquiryInfo(Long caregiverId) {
    Caregiver caregiver = caregiverRepository.findById(caregiverId)
            .orElseThrow(() -> new NotFoundException("caregiver: ", caregiverId, "존재하지 않는 간병인입니다."));

    CaregiverDTO.InquiryCaregiver inquiryCaregiver = new CaregiverDTO.InquiryCaregiver();
    inquiryCaregiver.setName(caregiver.getName());
    inquiryCaregiver.setPhone(caregiver.getPhone());
    inquiryCaregiver.setHospital(caregiver.getHospital());
    inquiryCaregiver.setPatientName(caregiver.getPatient().getName());

    return inquiryCaregiver;
  }

  @Transactional
  public CaregiverDTO.UpdateCaregiver updateInfo(Long caregiverId, CaregiverDTO.UpdateCaregiver updateCaregiver) {
    Caregiver caregiver = caregiverRepository.findById(caregiverId)
            .orElseThrow(() -> new NotFoundException("caregiver: ", caregiverId, "존재하지 않는 간병인입니다."));

    caregiver.setName(updateCaregiver.getName());
    caregiver.setPhone(updateCaregiver.getPhone());
    caregiver.setHospital(updateCaregiver.getHospital());
    caregiverRepository.save(caregiver);

    CaregiverDTO.UpdateCaregiver updatedCaregiver = new CaregiverDTO.UpdateCaregiver();
    updatedCaregiver.setName(caregiver.getName());
    updatedCaregiver.setPhone(caregiver.getPhone());
    updatedCaregiver.setHospital(caregiver.getHospital());

    return updatedCaregiver;
  }

  @Transactional(readOnly = true)
  public CaregiverDTO.GuardianProfile inquiryGuardianProfile(Long caregiverId) {
    Caregiver caregiver = caregiverRepository.findById(caregiverId)
            .orElseThrow(() -> new NotFoundException("caregiver", caregiverId, "존재하지 않는 간병인입니다."));

    if (caregiver.getGuardian() == null) {
      throw new NotFoundException("caregiver", caregiverId, "간병인에 연결된 보호자가 없습니다.");
    }

    CaregiverDTO.GuardianProfile guardianProfile = new CaregiverDTO.GuardianProfile();
    guardianProfile.setName(caregiver.getGuardian().getName());
    guardianProfile.setPhone(caregiver.getGuardian().getPhone());
    guardianProfile.setAddress(caregiver.getGuardian().getAddress());

    return guardianProfile;
  }
}
