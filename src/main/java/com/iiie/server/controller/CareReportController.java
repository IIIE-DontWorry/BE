package com.iiie.server.controller;

import com.iiie.server.domain.CareReport;
import com.iiie.server.service.CareReportService;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 해당 어노테이션 필수로 일단 달아주세요.
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
  public SuccessResponse<CareReport> initCareReport(@PathVariable Long careGiverId) {

    CareReport careReport = careReportService.initCareReport(careGiverId);
    return new SuccessResponse<>("최초 간병 보고서 생성 완료", careReport);
  }

  @DeleteMapping("/{careGiverId}/delete")
  @Operation(summary = "간병보고서 삭제(개발용)", description = "간병 보고서 하나를 삭제합니다.(개발용입니다.)")
  public SuccessResponse<Void> deleteCareReport(@PathVariable Long careGiverId) {
    careReportService.deleteCareReport(careGiverId);

    return new SuccessResponse<>("간병 보고서 삭제 완료", null);
  }
  /*
  @GetMapping("/{careReportId}")
  @Operation(summary = "간병 보고서 상세 조회", description = "")*/
}
