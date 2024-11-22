package com.iiie.server.controller;

import com.iiie.server.dto.CareReportDTO.CareReportPatchRequest;
import com.iiie.server.dto.CareReportDTO.CareReportResponse;
import com.iiie.server.service.CareReportService;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/care-reports")
public class CareReportController {

  private final CareReportService careReportService;

  public CareReportController(CareReportService careReportService) {
    this.careReportService = careReportService;
  }

  @PostMapping("/{careGiverId}/init")
  @Operation(
      summary = "간병 보고서 생성",
      description = "오늘 날짜의 초기의 빈 상태의 보고서를 생성합니다.(만약 이미 오늘 날짜의 보고서가 존재하면, 수정으로)")
  public SuccessResponse<CareReportResponse> initCareReport(@PathVariable Long careGiverId) {

    CareReportResponse result = careReportService.initCareReport(careGiverId);
    return new SuccessResponse<>("최초 간병 보고서 생성 완료", result);
  }

  @DeleteMapping("/{careReportId}/delete")
  @Operation(summary = "간병보고서 삭제(개발용)", description = "간병 보고서 PK를 통해 하나를 삭제합니다.(개발용입니다.)")
  public SuccessResponse<Void> deleteCareReport(@PathVariable Long careReportId) {
    careReportService.deleteCareReport(careReportId);

    return new SuccessResponse<>("간병 보고서 삭제 완료", null);
  }

  @GetMapping("/{careReportId}")
  @Operation(summary = "간병 보고서 상세 조회", description = "특정 간병 보고서 상세 조회합니다.")
  public SuccessResponse<CareReportResponse> getCareReportDetail(@PathVariable Long careReportId) {
    CareReportResponse careReportDetail = careReportService.getCareReportDetail(careReportId);

    return new SuccessResponse<>("간병 보고서 상세 조회 완료", careReportDetail);
  }

  @PatchMapping("/{careReportId}")
  @Operation(summary = "간병 보고서 업데이트", description = "간병 보고서 내용을 선택적으로 수정합니다.")
  public SuccessResponse<CareReportResponse> patchCareReport(
      @PathVariable Long careReportId, @RequestBody CareReportPatchRequest request) {
    CareReportResponse careReport = careReportService.patchCareReport(careReportId, request);

    return new SuccessResponse<>("간병보고서 업데이트 성공", careReport);
  }
}
