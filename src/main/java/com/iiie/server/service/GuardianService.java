package com.iiie.server.service;

import com.iiie.server.domain.Guardian;
import com.iiie.server.domain.Patient;
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
}
