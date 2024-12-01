package com.iiie.server.service;

import com.iiie.server.convertor.ConvertorDTO;
import com.iiie.server.domain.Guardian;
import com.iiie.server.domain.Patient;
import com.iiie.server.dto.GuardianDTO;
import com.iiie.server.dto.GuardianDTO.InquiryGuardian;
import com.iiie.server.dto.GuardianDTO.InquiryGuardian.GuardianInfo;
import com.iiie.server.dto.GuardianDTO.InquiryGuardian.PatientInfo;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.GuardianRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GuardianService {

  private final GuardianRepository guardianRepository;

  public GuardianService(GuardianRepository guardianRepository) {
    this.guardianRepository = guardianRepository;
  }

  @Transactional
  public Guardian createGuardian(
      String name,
      String phone,
      String address,
      String patientRequestName,
      Integer patientAge,
      String diseaseName,
      String hospitalName) {
    // TODO: 환자는 회원가입 로직이 없음 보호자한테 완전 의존 보호자 가입에 의해서 생성됨
    Patient patient =
        Patient.builder()
            .name(patientRequestName)
            .age(patientAge)
            .diseaseName(diseaseName)
            .hospitalName(hospitalName)
            .kakaoId(999L) // TODO: 임시값 넣어둠
            .build();

    Guardian guardian =
        Guardian.builder()
            .name(name)
            .phone(phone)
            .address(address)
            .kakaoId(999L) // TODO: 임시값 넣어둠
            .mannerScore(0.0)
            .uniqueCode(UUID.randomUUID())
            .build();
    guardian.setPatient(patient);

    return guardianRepository.save(guardian);
  }

  @Transactional(readOnly = true)
  public GuardianDTO.InquiryGuardian inquiryInfo(Long guardianId) {
    Guardian guardian =
        guardianRepository
            .findById(guardianId)
            .orElseThrow(() -> new NotFoundException("guardian: ", guardianId, "존재하지 않는 보호자입니다."));

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

  @Transactional
  public GuardianDTO.UpdateGuardian updateInfo(
      Long guardianId, GuardianDTO.UpdateGuardian updateGuardian) {
    Guardian guardian =
        guardianRepository
            .findById(guardianId)
            .orElseThrow(() -> new NotFoundException("guardian: ", guardianId, "존재하지 않는 보호자입니다."));

    guardian.setName(updateGuardian.getName());
    guardian.setPhone(updateGuardian.getPhone());
    guardian.setAddress(updateGuardian.getAddress());
    guardianRepository.save(guardian);

    GuardianDTO.UpdateGuardian updatedGuardian = new GuardianDTO.UpdateGuardian();
    updatedGuardian.setName(guardian.getName());
    updatedGuardian.setPhone(guardian.getPhone());
    updatedGuardian.setAddress(guardian.getAddress());

    return updatedGuardian;
  }

  @Transactional
  public void deleteGuardian(Long guardianId) {
    Guardian guardian =
        guardianRepository
            .findById(guardianId)
            .orElseThrow(() -> new NotFoundException("guardian", guardianId, "존재하지 않는 보호자입니다."));

    // 간병인과의 참조를 제거
    if (guardian.getCaregiver() != null) {
      guardian.setCaregiver(null);
    }

    // 보호자 삭제
    guardianRepository.delete(guardian);
  }

  @Transactional(readOnly = true)
  public GuardianDTO.CaregiverProfile inquiryCaregiverProfile(Long guardianId) {
    Guardian guardian =
        guardianRepository
            .findById(guardianId)
            .orElseThrow(() -> new NotFoundException("guardian", guardianId, "존재하지 않는 보호자입니다."));

    if (guardian.getCaregiver() == null) {
      throw new NotFoundException("guardian", guardianId, "보호자에 연결된 간병인이 없습니다.");
    }

    GuardianDTO.CaregiverProfile caregiverProfile = new GuardianDTO.CaregiverProfile();
    caregiverProfile.setName(guardian.getCaregiver().getName());
    caregiverProfile.setPhone(guardian.getCaregiver().getPhone());
    caregiverProfile.setHospital(guardian.getCaregiver().getHospital());

    return caregiverProfile;
  }
}
