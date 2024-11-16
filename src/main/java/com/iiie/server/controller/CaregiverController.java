package com.iiie.server.controller;

import com.iiie.server.domain.Caregiver;
import com.iiie.server.dto.CaregiverDTO;
import com.iiie.server.service.CaregiverService;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/care-givers")
public class CaregiverController {

  private final CaregiverService caregiverService;

  public CaregiverController(CaregiverService caregiverService) {
    this.caregiverService = caregiverService;
  }

  @PostMapping
  @Operation(summary = "간병인 회원가입(카카오 미완)", description = "간병인이 보호자 인증코드를 가지고 회원가입합니다.(카카오 미완)")
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
}