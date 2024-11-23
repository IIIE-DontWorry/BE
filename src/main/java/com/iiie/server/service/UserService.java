package com.iiie.server.service;

import com.iiie.server.domain.CareerHistory;
import com.iiie.server.domain.Caregiver;
import com.iiie.server.domain.Guardian;
import com.iiie.server.domain.MedicationCheck;
import com.iiie.server.domain.Patient;
import com.iiie.server.dto.CaregiverDTO.CreationCaregiver;
import com.iiie.server.dto.GuardianAndPatientDTO.CreationRequest;
import com.iiie.server.dto.GuardianDTO.CreationGuardian;
import com.iiie.server.dto.PatientDTO.CreationPatient;
import com.iiie.server.dto.UserDTO;
import com.iiie.server.dto.UserDTO.Response;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.CaregiverRepository;
import com.iiie.server.repository.GuardianRepository;
import com.iiie.server.security.JwtTokenProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final Double DEFAULT_MANNER_SCORE = 0.0;

  private final JwtTokenProvider jwtTokenProvider;
  private final GuardianRepository guardianRepository;
  private final CaregiverRepository careGiverRepository;

  public UserService(
      JwtTokenProvider jwtTokenProvider,
      GuardianRepository guardianRepository,
      CaregiverRepository careGiverRepository) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.guardianRepository = guardianRepository;
    this.careGiverRepository = careGiverRepository;
  }

  @Transactional
  public UserDTO.Response caregiverValidation(
      HashMap<String, Object> userInfo, CreationCaregiver request) {
    Long kakaoId = (Long) userInfo.get("id");
    Optional<Caregiver> optionalCaregiver = careGiverRepository.findByKakaoId(kakaoId);

    if (optionalCaregiver.isEmpty()) {
      Caregiver newCaregiver = registerCaregiver(kakaoId, request);

      return UserDTO.Response.builder()
          .accessToken(jwtTokenProvider.createAccessToken(newCaregiver.getId()))
          .build();
    } else {
      return UserDTO.Response.builder()
          .accessToken(jwtTokenProvider.createAccessToken(optionalCaregiver.get().getId()))
          .build();
    }
  }

  private Caregiver registerCaregiver(Long kakaoId, CreationCaregiver request) {
    Guardian guardian =
        guardianRepository
            .findByUniqueCode(request.getGuardianUniqueCode())
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "guardian", request.getGuardianUniqueCode(), "코드와 일치하는 보호자가 존재하지 않습니다."));

    List<CareerHistory> careerHistories =
        request.getCareerHistories().stream()
            .map(
                careerHistoryDescription ->
                    CareerHistory.builder().description(careerHistoryDescription).build())
            .collect(Collectors.toCollection(ArrayList::new));

    Caregiver caregiver =
        Caregiver.builder()
            .name(request.getName())
            .phone(request.getPhone())
            .hospital(request.getHospital())
            .kakaoId(kakaoId)
            .mannerScore(DEFAULT_MANNER_SCORE)
            .careerHistories(careerHistories)
            .build();

    guardian.setCaregiver(caregiver);
    caregiver.setPatient(guardian.getPatient());
    caregiver.addCareerHistories(careerHistories);

    return careGiverRepository.save(caregiver);
  }

  @Transactional
  public Response guardianValidation(HashMap<String, Object> userInfo, CreationRequest request) {
    Long kakaoId = (Long) userInfo.get("id");
    Optional<Guardian> optionalGuardian = guardianRepository.findByKakaoId(kakaoId);

    if (optionalGuardian.isEmpty()) {
      Guardian newGuardian = registerGuardian(kakaoId, request);

      return UserDTO.Response.builder()
          .accessToken(jwtTokenProvider.createAccessToken(newGuardian.getId()))
          .build();
    } else {
      return UserDTO.Response.builder()
          .accessToken(jwtTokenProvider.createAccessToken(optionalGuardian.get().getId()))
          .build();
    }
  }

  private Guardian registerGuardian(Long kakaoId, CreationRequest request) {
    CreationGuardian creationGuardian = request.getCreationGuardian();
    CreationPatient creationPatient = request.getCreationPatient();

    List<MedicationCheck> medicationChecks =
        creationPatient.getMedicationInfos().stream()
            .map(
                medicationInfo ->
                    MedicationCheck.builder()
                        .name(medicationInfo.getName())
                        .morningTakenStatus(false)
                        .afternoonTakenStatus(false)
                        .eveningTakenStatus(false)
                        .build())
            .collect(Collectors.toCollection(ArrayList::new));

    Patient patient =
        Patient.builder()
            .name(creationPatient.getName())
            .age(creationPatient.getAge())
            .diseaseName(creationPatient.getDiseaseName())
            .hospitalName(creationPatient.getHospitalName())
            .address(creationPatient.getAddress())
            .kakaoId(kakaoId)
            .build();
    patient.addMedicationChecks(medicationChecks);

    Guardian guardian =
        Guardian.builder()
            .name(creationGuardian.getName())
            .phone(creationGuardian.getPhone())
            .address(creationGuardian.getAddress())
            .kakaoId(kakaoId)
            .mannerScore(DEFAULT_MANNER_SCORE)
            .uniqueCode(UUID.randomUUID())
            .build();

    guardian.setPatient(patient);

    return guardianRepository.save(guardian);
  }
}
