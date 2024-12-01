package com.iiie.server.controller;

import com.iiie.server.domain.Guardian;
import com.iiie.server.dto.CaregiverDTO.InquiryCaregiver;
import com.iiie.server.dto.GuardianAndPatientDTO;
import com.iiie.server.dto.GuardianDTO;
import com.iiie.server.dto.GuardianDTO.CreationGuardian;
import com.iiie.server.dto.PatientDTO.CreationPatient;
import com.iiie.server.service.GuardianService;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  @Operation(summary = "보호자 회원가입(개발용)", description = "보호자는 환자 정보와 함께 회원가입합니다.(카카오 미완)")
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

  @GetMapping("/myPage/{guardianId}")
  @Operation(summary = "마이 페이지 조회", description = "보호자는 자신의 기본 정보를 확인할 수 있다.")
  public SuccessResponse<GuardianDTO.InquiryGuardian> inquiryInfo(@PathVariable Long guardianId) {
    GuardianDTO.InquiryGuardian inquiryInfo = guardianService.inquiryInfo(guardianId);
    return new SuccessResponse<>("보호자 마이 페이지 조회 완료", inquiryInfo);
  }

  @PostMapping("/myPage/update/{guardianId}")
  @Operation(summary = "보호자 정보 수정", description = "보호자는 자신의 기본 정보를 수정할 수 있다.")
  public SuccessResponse<GuardianDTO.UpdateGuardian> updateInfo(
      @PathVariable Long guardianId, @RequestBody GuardianDTO.UpdateGuardian updateGuardian) {
    GuardianDTO.UpdateGuardian updateInfo = guardianService.updateInfo(guardianId, updateGuardian);
    return new SuccessResponse<>("보호자 정보 수정 완료", updateInfo);
  }

  @PostMapping("/myPage/delete/{guardianId}")
  @Operation(summary = "보호자 탈퇴", description = "보호자는 자신의 계정을 삭제할 수 있다.")
  public SuccessResponse<String> deleteGuardian(@PathVariable Long guardianId) {
    guardianService.deleteGuardian(guardianId);
    return new SuccessResponse<>("보호자 탈퇴 완료", null);
  }

  @GetMapping("/myPage/caregiver/{guardianId}")
  @Operation(summary = "간병인 정보 조회", description = "보호자는 연결된 간병인의 기본 정보를 조회 할 수 있다.")
  public SuccessResponse<InquiryCaregiver> inquiryCaregiverProfile(@PathVariable Long guardianId) {
    InquiryCaregiver caregiverProfile = guardianService.inquiryCaregiverProfile(guardianId);
    return new SuccessResponse<>("간병인 정보 조회 완료", caregiverProfile);
  }
}
