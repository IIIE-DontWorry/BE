package com.iiie.server.controller;

import com.iiie.server.dto.MedicationCheckDTO.MedicationCheckCreationRequest;
import com.iiie.server.service.MedicationCheckService;
import com.iiie.server.utils.SuccessResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medication-checks")
public class MedicationCheckController {

  private final MedicationCheckService medicationCheckService;

  public MedicationCheckController(MedicationCheckService medicationCheckService) {
    this.medicationCheckService = medicationCheckService;
  }

  @PostMapping("/{guardianId}")
  public SuccessResponse<Void> createMedicationChecks(
      @PathVariable(name = "guardianId") Long guardianId,
      @RequestBody MedicationCheckCreationRequest request) {
    medicationCheckService.createMedicationChecks(guardianId, request);

    return new SuccessResponse<>("투약 정보 생성 완료", null);
  }
}
