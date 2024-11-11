package com.iiie.server.controller;

import com.iiie.server.domain.CareReport;
import com.iiie.server.dto.CareReportDTO.CareReportRequest;
import com.iiie.server.service.CareReportService;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //해당 어노테이션 필수로 일단 달아주세요.
@RequestMapping("/care-report")
public class CareReportController {

    private final CareReportService careReportService;

    public CareReportController(CareReportService careReportService) {
        this.careReportService = careReportService;
    }

    //간병인은 시간에 따른 일지 내용 추가 버튼을 눌러 키보드로 내용을 추가하여 보고서에 반영한다.
    //(TODO: 검증하려면 생성부터 해야할듯)
    @PostMapping("/{careGiverID}")
    @Operation(summary = "간병 보고서 작성", description = "간병인이 간병 보고서를 작성합니다.")
    public SuccessResponse<CareReport> createCareReport(
            @PathVariable Long careGiverID,
            @RequestBody  CareReportRequest careReportRequest) {
        CareReport currentThreeReports = careReportService.createCareReport(careGiverID, careReportRequest.getSpecialNote(), careReportRequest.getDescription().getCareSchedulesDescription());

        return new SuccessResponse<>("간병 보고서 작성 성공", currentThreeReports);
    }
}
