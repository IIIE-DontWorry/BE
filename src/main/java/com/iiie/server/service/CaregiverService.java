package com.iiie.server.service;

import com.iiie.server.convertor.ConvertorDTO;
import com.iiie.server.domain.CareerHistory;
import com.iiie.server.domain.Caregiver;
import com.iiie.server.domain.Guardian;
import com.iiie.server.domain.Patient;
import com.iiie.server.dto.CaregiverDTO;
import com.iiie.server.dto.CaregiverDTO.InquiryCaregiver;
import com.iiie.server.dto.GuardianDTO.InquiryGuardian;
import com.iiie.server.dto.GuardianDTO.InquiryGuardian.GuardianInfo;
import com.iiie.server.dto.GuardianDTO.InquiryGuardian.PatientInfo;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.CaregiverRepository;
import com.iiie.server.repository.GuardianRepository;
import java.util.List;
import java.util.UUID;

import com.iiie.server.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CaregiverService {

  private final GuardianRepository guardianRepository;
  private final CaregiverRepository caregiverRepository;
  private final PatientRepository patientRepository;

  public CaregiverService(
          GuardianRepository guardianRepository, CaregiverRepository caregiverRepository, PatientRepository patientRepository) {
    this.guardianRepository = guardianRepository;
    this.caregiverRepository = caregiverRepository;
    this.patientRepository = patientRepository;
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

    return caregiverRepository.save(caregiver);
  }

  @Transactional(readOnly = true)
  public CaregiverDTO.UpdateCaregiver inquiryInfo(Long caregiverId, Long patientId) {
    Caregiver caregiver =
        caregiverRepository
            .findById(caregiverId)
            .orElseThrow(() -> new NotFoundException("caregiver: ", caregiverId, "존재하지 않는 간병인입니다."));
    Patient patient = patientRepository
            .findById(patientId)
            .orElseThrow(() -> new NotFoundException("patient: ", patientId, "존재하지 않는 환자입니다."));

    // DTO 반환
    CaregiverDTO.UpdateCaregiver updatedCaregiver = new CaregiverDTO.UpdateCaregiver();
    updatedCaregiver.setCaregiverName(caregiver.getName());
    updatedCaregiver.setPhone(caregiver.getPhone());
    updatedCaregiver.setHospital(caregiver.getHospital());
    updatedCaregiver.setCarrierHistory(caregiver.getCareerHistories().stream()
            .map(CareerHistory::getDescription)
            .toList());
    updatedCaregiver.setPatientName(patient.getName());
    updatedCaregiver.setAge(patient.getAge());
    updatedCaregiver.setDiseaseName(patient.getDiseaseName());
    updatedCaregiver.setHospitalName(patient.getHospitalName());
    updatedCaregiver.setAddress(patient.getAddress());

    return updatedCaregiver;
  }

  @Transactional
  public CaregiverDTO.UpdateCaregiver updateInfo(Long caregiverId, Long patientId, CaregiverDTO.UpdateCaregiver updateCaregiver) {
    Caregiver caregiver = caregiverRepository
            .findById(caregiverId)
            .orElseThrow(() -> new NotFoundException("caregiver: ", caregiverId, "존재하지 않는 간병인입니다."));

    Patient patient = patientRepository
            .findById(patientId)
            .orElseThrow(() -> new NotFoundException("patient: ", patientId, "존재하지 않는 환자입니다."));

    // Caregiver 기본 정보 업데이트
    caregiver.setName(updateCaregiver.getCaregiverName());
    caregiver.setPhone(updateCaregiver.getPhone());
    caregiver.setHospital(updateCaregiver.getHospital());

    // CareerHistory description 덮어쓰기
    List<CareerHistory> existingHistories = caregiver.getCareerHistories();
    List<String> newDescriptions = updateCaregiver.getCarrierHistory();

    if (existingHistories.size() == newDescriptions.size()) {
      for (int i = 0; i < existingHistories.size(); i++) {
        existingHistories.get(i).setDescription(newDescriptions.get(i));
      }
    } else {
      throw new IllegalArgumentException("CareerHistory 리스트 크기가 맞지 않습니다.");
    }

    caregiverRepository.save(caregiver);

    // Patient 정보 업데이트
    patient.setName(updateCaregiver.getPatientName());
    patient.setAge(updateCaregiver.getAge());
    patient.setDiseaseName(updateCaregiver.getDiseaseName());
    patient.setHospitalName(updateCaregiver.getHospitalName());
    patient.setAddress(updateCaregiver.getAddress());
    patientRepository.save(patient);

    // DTO 반환
    CaregiverDTO.UpdateCaregiver updatedCaregiver = new CaregiverDTO.UpdateCaregiver();
    updatedCaregiver.setCaregiverName(caregiver.getName());
    updatedCaregiver.setPhone(caregiver.getPhone());
    updatedCaregiver.setHospital(caregiver.getHospital());
    updatedCaregiver.setCarrierHistory(caregiver.getCareerHistories().stream()
            .map(CareerHistory::getDescription)
            .toList());
    updatedCaregiver.setPatientName(patient.getName());
    updatedCaregiver.setAge(patient.getAge());
    updatedCaregiver.setDiseaseName(patient.getDiseaseName());
    updatedCaregiver.setHospitalName(patient.getHospitalName());
    updatedCaregiver.setAddress(patient.getAddress());

    return updatedCaregiver;
  }

  @Transactional
  public void deleteCaregiver(Long caregiverId) {
    Caregiver caregiver =
        caregiverRepository
            .findById(caregiverId)
            .orElseThrow(() -> new NotFoundException("caregiver", caregiverId, "존재하지 않는 간병인입니다."));

    Guardian guardian = caregiver.getGuardian();
    if (guardian != null) {
      guardian.setCaregiver(null);
      guardianRepository.save(guardian); // 보호자의 caregiver 참조를 제거
    }

    caregiverRepository.delete(caregiver);
  }

  @Transactional(readOnly = true)
  public InquiryGuardian inquiryGuardianProfile(Long caregiverId) {
    Caregiver caregiver =
        caregiverRepository
            .findById(caregiverId)
            .orElseThrow(() -> new NotFoundException("caregiver", caregiverId, "존재하지 않는 간병인입니다."));

    Guardian guardian = caregiver.getGuardian();
    if (guardian == null) {
      throw new NotFoundException("caregiver", caregiverId, "간병인에 연결된 보호자가 없습니다.");
    }

    GuardianInfo guardianInfo =
        GuardianInfo.builder()
            .name(guardian.getName())
            .phone(guardian.getPhone())
            .address(guardian.getAddress())
            .build();

    Patient patient = guardian.getPatient();

    PatientInfo patientInfo =
        PatientInfo.builder()
            .name(patient.getName())
            .age(patient.getAge())
            .diseaseName(patient.getDiseaseName())
            .hospitalName(patient.getHospitalName())
            .MedicationInfos(ConvertorDTO.toMedicationInfos(patient.getMedicationChecks()))
            .build();

    return InquiryGuardian.builder().guardianInfo(guardianInfo).patientInfo(patientInfo).build();
  }
}
