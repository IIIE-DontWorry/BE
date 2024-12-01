package com.iiie.server.controller;

import com.iiie.server.domain.Caregiver;
import com.iiie.server.dto.CaregiverDTO;
import com.iiie.server.dto.GuardianDTO.InquiryGuardian;
import com.iiie.server.service.CaregiverService;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/care-givers")
public class CaregiverController {

  private final CaregiverService caregiverService;

  public CaregiverController(CaregiverService caregiverService) {
    this.caregiverService = caregiverService;
  }

  @PostMapping
  @Operation(summary = "간병인 회원가입(개발용)", description = "간병인이 보호자 인증코드를 가지고 회원가입합니다.(카카오 미완)")
  SuccessResponse<Caregiver> createCaregiver(@RequestBody CaregiverDTO.CreationCaregiver request) {
    Caregiver caregiver =
        caregiverService.createCaregiver(
            request.getName(),
            request.getPhone(),
            request.getHospital(),
            request.getCareerHistories(),
            request.getGuardianUniqueCode());
    return new SuccessResponse<>("간병인이 가입이 완료되었습니다.", caregiver);
  }

  @GetMapping("/myPage/{caregiverId}")
  @Operation(summary = "마이 페이지 조회", description = "간병인은 자신의 기본 정보를 확인할 수 있다.")
  public SuccessResponse<CaregiverDTO.InquiryCaregiver> inquiryInfo(
      @PathVariable Long caregiverId) {
    CaregiverDTO.InquiryCaregiver inquiryInfo = caregiverService.inquiryInfo(caregiverId);
    return new SuccessResponse<>("간병인 마이 페이지 조회 완료", inquiryInfo);
  }

  @PostMapping("/myPage/update/{caregiverId}")
  @Operation(summary = "간병인 정보 수정", description = "간병인은 자신의 기본 정보를 수정할 수 있다.")
  public SuccessResponse<CaregiverDTO.UpdateCaregiver> updateInfo(
      @PathVariable Long caregiverId, @RequestBody CaregiverDTO.UpdateCaregiver updateCaregiver) {
    CaregiverDTO.UpdateCaregiver updateInfo =
        caregiverService.updateInfo(caregiverId, updateCaregiver);
    return new SuccessResponse<>("간병인 정보 수정 완료", updateInfo);
  }

  @DeleteMapping("/myPage/delete/{caregiverId}")
  @Operation(summary = "간병인 탈퇴", description = "간병인은 자신의 계정을 삭제할 수 있다.")
  public SuccessResponse<String> deleteCaregiver(@PathVariable Long caregiverId) {
    caregiverService.deleteCaregiver(caregiverId);
    return new SuccessResponse<>("간병인 탈퇴 완료", null);
  }

  @GetMapping("/myPage/guardianProfile/{caregiverId}")
  @Operation(summary = "보호자 정보 조회", description = "간병인은 연결된 보호자의 기본 정보를 조회할 수 있다.")
  public SuccessResponse<InquiryGuardian> inquiryGuardianProfile(@PathVariable Long caregiverId) {
    InquiryGuardian guardianProfile = caregiverService.inquiryGuardianProfile(caregiverId);
    return new SuccessResponse<>("보호자 정보 조회 완료", guardianProfile);
  }
}
