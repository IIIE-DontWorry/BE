package com.iiie.server.controller;

import com.iiie.server.dto.GuardianRequestDTO.GuardianRequestCreationRequest;
import com.iiie.server.service.GuardianRequestService;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guardian_requests")
public class GuardianRequestController {

  private final GuardianRequestService guardianRequestService;

  public GuardianRequestController(GuardianRequestService guardianRequestService) {
    this.guardianRequestService = guardianRequestService;
  }

  @PostMapping("/{guardianId}")
  @Operation(
      summary = "특별 요청 사항을 등록합니다.",
      description = "보호자 마이페이지에서 특별 요청사항을 추가할 때마다 새로운 요청 사항이 생성되고 나머지는 old 요청이 됩니다.")
  public SuccessResponse<Void> createGuardianRequest(
      @RequestBody GuardianRequestCreationRequest request,
      @PathVariable(name = "guardianId") Long guardianId) {
    guardianRequestService.createGuardianRequest(guardianId, request);
    return new SuccessResponse<>("특별 요청사항 생성 완료", null);
  }
}
