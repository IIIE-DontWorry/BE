package com.iiie.server.controller;

import com.iiie.server.dto.CaregiverDTO;
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

  @GetMapping("/myPage/{caregiverId}")
  @Operation(summary = "마이 페이지 조회", description = "간병인은 자신의 기본 정보를 확인할 수 있다.")
  public SuccessResponse<CaregiverDTO.InquiryCaregiver> inquiryInfo(@PathVariable Long caregiverId) {
    CaregiverDTO.InquiryCaregiver inquiryInfo = caregiverService.inquiryInfo(caregiverId);
    return new SuccessResponse<>("간병인 마이 페이지 조회 완료", inquiryInfo);
  }

  @PostMapping("/myPage/update/{caregiverId}")
  @Operation(summary = "간병인 정보 수정", description = "간병인은 자신의 기본 정보를 수정할 수 있다.")
  public SuccessResponse<CaregiverDTO.UpdateCaregiver> updateInfo(@PathVariable Long caregiverId, @RequestBody CaregiverDTO.UpdateCaregiver updateCaregiver) {
    CaregiverDTO.UpdateCaregiver updateInfo = caregiverService.updateInfo(caregiverId, updateCaregiver);
    return new SuccessResponse<>("간병인 정보 수정 완료", updateInfo);
  }

  @GetMapping("/myPage/guardianProfile/{caregiverId}")
  @Operation(summary = "보호자 정보 조회", description = "간병인은 연결된 보호자의 기본 정보를 조회할 수 있다.")
  public SuccessResponse<CaregiverDTO.GuardianProfile> inquiryGuardianProfile(@PathVariable Long caregiverId) {
    CaregiverDTO.GuardianProfile guardianProfile = caregiverService.inquiryGuardianProfile(caregiverId);
    return new SuccessResponse<>("보호자 정보 조회 완료", guardianProfile);
  }
}
