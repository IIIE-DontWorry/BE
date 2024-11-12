package com.iiie.server.controller;

import com.iiie.server.domain.Guardian;
import com.iiie.server.dto.GuardianAndPatientDTO;
import com.iiie.server.dto.GuardianDTO.CreationGuardian;
import com.iiie.server.dto.PatientDTO.CreationPatient;
import com.iiie.server.service.GuardianService;
import com.iiie.server.utils.SuccessResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guardian")
public class GuardianController {

  private final GuardianService guardianService;

  public GuardianController(GuardianService guardianService) {
    this.guardianService = guardianService;
  }

  @PostMapping
  public SuccessResponse<Guardian> createGuardian(
      @RequestBody GuardianAndPatientDTO.CreationRequest request) {

    CreationGuardian guardianRequest = request.getCreationGuardian();
    CreationPatient patientRequest = request.getCreationPatient();

    Guardian guardian =
        guardianService.createGuardian(
            guardianRequest.getName(),
            guardianRequest.getPhone(),
            guardianRequest.getAddress(),
            patientRequest.getName(),
            patientRequest.getAge(),
            patientRequest.getDiseaseName(),
            patientRequest.getHospitalName());

    return new SuccessResponse<>("환자 딸린 보호자 생성 완료", guardian);
  }
}
